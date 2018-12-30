package com.gd.oa;

import com.gd.oa.encrypt.WXBizMsgCrypt;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.json.JsonArray;
import jodd.json.JsonObject;
import jodd.json.JsonParser;
import jodd.util.StringUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
// This means that this class is a Controller
@RequestMapping(path = "/oa")
// This means URL's start with /demo (after Application path)
public class MainController {
    @Value("${gd_api_timeout}")
    private int gd_api_timeout;

    @Value("${gd_api_connect_timeout}")
    private int gd_api_connect_timeout;

    @Value("${url2browser}")
    private String url2browser;

    @Value("${enable_paymentcheck}")
    private String enable_paycmentcheck;

    @Value("${token}")
    private String token;

    @Value("${encodingAESKey}")
    private String encodingAESKey;

    @Value("${appid}")
    private String appid;

    @Value("${appsecret}")
    private String appsecret;


    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private static TokenRefresher tf = null;

    public MainController() throws IOException {
        tf = new TokenRefresher();
        FAQ.loadJSON();
    }

    /**
     * Handle response for WeChat validation URL.
     *
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
    public @ResponseBody
    String returnEchostr(HttpServletRequest req, HttpServletResponse resp) {
        String sid = RandomStringUtils.randomAlphanumeric(32);
        String[] echostr = req.getParameterValues("echostr");
        if (echostr != null && echostr.length > 0) {
            log.info(String.format("%s, Catch echostr %s", sid, echostr[0]));
            return echostr[0];
        }
        return "";
    }

    /**
     * General WeChat msg handling and procession method.
     *
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "text/xml; charset=utf-8")
    public @ResponseBody
    void handleMSG(HttpServletRequest req, HttpServletResponse resp) {
        long time = System.currentTimeMillis();
        String sid = RandomStringUtils.randomAlphanumeric(32);

        String encrypt_type, msg_signature, timestamp, nonce;
        try {
            encrypt_type = req.getParameter("encrypt_type");
            msg_signature = req.getParameter("msg_signature");
            nonce = req.getParameter("nonce");
            timestamp = req.getParameter("timestamp");

            WXBizMsgCrypt wxMsgCrypt = new WXBizMsgCrypt(token, encodingAESKey, appid);

            OAMsg msg = OAMsg.toOAMsg(req.getInputStream(), encrypt_type, msg_signature, timestamp, nonce, wxMsgCrypt);
            if (msg == null) {
                log.error(String.format("%s, Req Message: %s. Get request body failed!", sid, req.getQueryString()));
                log.info(String.format("%s, handleMSG done cost time ( %s ) ms", sid, (System.currentTimeMillis() - time)));

                return;
            }

            log.info(String.format("%s, Req Message: %s, [%s]", sid, req.getQueryString(), msg.toString()));

            if ("event".equals(msg.msgType) && !"subscribe".equals(msg.event)) {
                //Handle new user subscribe event, hardcode the video information here.
                log.info(String.format("%s, unregistered operation, return directly.", sid));
                log.info(String.format("%s, handleMSG done cost time ( %s ) ms", sid, (System.currentTimeMillis() - time)));

                return;
            }

            //Get user wechat language configuration.
            JsonObject userInfo = getWeChatInfo(msg.fromUserName);
            String lang = userInfo.getString("language");
            log.info(String.format("%s, Current user info: %s", sid, userInfo.toString()));

            if (!FAQ.getFaqMap().containsKey(lang)) {
                log.warn(String.format("%s, User language is %s, use %s as default.", sid, lang, FAQ.getDefaultLang()));
                lang = FAQ.getDefaultLang();
            }

            //Get the voice text content first.
            if ("voice".equals(msg.msgType)) {
                msg.content = msg.recognition;
                if (StringUtils.isEmpty(msg.content) == false) {
                    msg.content = msg.content.replaceAll("[。，、！]", "");
                }
                msg.msgType = "text";
            }

            FAQ faq = FAQ.getFAQByLang(lang);

            //Handling user message logic...
            if ("event".equals(msg.msgType) && "subscribe".equals(msg.event)) {
                //Handle new user subscribe event, the video info is configured from faq.json
                log.info(String.format("%s, Send back welcome information.", sid));
//                msg.mediaID = faq.getWelcome_media_id();
//                msg.title = faq.getWelcome_title();
//                msg.description = faq.getWelcome_desc();
//                msg.msgType = "video";

                msg.msgType = "news";
                OAMsg.Article article = new OAMsg.Article();
                article.title = faq.getWelcome_title();
                article.desc = faq.getWelcome_desc();
                article.picURL = faq.getWelcome_media_id();
                article.url = faq.getWelcome_url();
                msg.articles.add(article);

            } else if ("myinfo".equals(msg.content)) {
                //Handle user info query request.
                msg.msgType = "text";
                msg.content = getWeChatInfo(msg.fromUserName).toString();

            } else if (msg.content.startsWith("video ") || msg.content.startsWith("image ")) {
                msg.mediaID = StringUtils.substringAfter(msg.content, " ");
                msg.title = "Your video";
                msg.description = "Your video";
                msg.msgType = msg.content.startsWith("video ") ? "video" : "image";

            } else {
                log.info(String.format("%s, Procession FAQ logic...", sid));
                List<FAQ.FAQsBean> matchFAQs = FAQ.search(msg.content, lang);

                if (matchFAQs.size() > 0) {
                    buildOAMsg4FAQ(msg, matchFAQs, true, lang);
                } else {
                    buildOAMsg4FAQ(msg, FAQ.getDefaultFAQs(lang), false, lang);
                }
            }

            OutputStream out = resp.getOutputStream();
            resp.setContentType("text/xml");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);

            OAMsg msg_resp = msg.clone();
            msg_resp.createTime = System.currentTimeMillis() / 1000;

            String response = msg_resp.toXMLString(wxMsgCrypt);
            out.write(response.getBytes("UTF-8"));
            log.info(String.format("%s, Response msg: %s", sid, response));
            log.info(String.format("%s, Response msg length: %s", sid, response.length()));

            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(String.format("%s, error: %s", sid, e.getMessage()));
            e.printStackTrace();
        }

        log.info(String.format("%s, handleMSG done cost time ( %s ) ms", sid, (System.currentTimeMillis() - time)));
    }

    /**
     * Handle OA login GD and redirect to GoDaddy page.
     *
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/redir/{app}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
    public @ResponseBody
    void oaLoginRedirect(HttpServletRequest req, HttpServletResponse resp, @PathVariable(required = true) String app) throws IOException {
        long time = System.currentTimeMillis();
        String sid = RandomStringUtils.randomAlphanumeric(32);
        log.info(String.format("%s, Starting OA redirect to GoDaddy: %s?%s", sid, req.getRequestURL(), req.getQueryString()));

        String openId = "";
        String code = req.getParameter("code");
        if (StringUtils.isEmpty(code)) {
            openId = req.getParameter("oid");
        } else {
            openId = getOpenID(code);
        }

        log.info(String.format("%s, Get OpenID from Code: %s >> %s", sid, code, openId));

        String path = StringUtils.isEmpty(req.getParameter("path")) ? "" : req.getParameter("path");

        if (StringUtils.isEmpty(openId)) {
            redirectToRelogin(resp, sid);
            log.info(String.format("%s, Redirection done cost time ( %s ) ms", sid, (System.currentTimeMillis() - time)));
            return;
        }
        log.info(String.format("%s, Got WeChat OA openID: (%s)", sid, openId));

        Map jwt = getJWTByOA(openId, sid);
        if (jwt == null || !jwt.containsKey("data") || !jwt.containsKey("code")) {
            log.error(String.format("%s, Empty jwt token.", sid));
            redirectToRelogin(resp, sid);
            log.info(String.format("%s, Redirection done cost time ( %s ) ms", sid, (System.currentTimeMillis() - time)));
            return;
        }

        log.info(String.format("%s, Got %s JWT token: code(%s), data(%s)", sid, openId, jwt.get("code").toString(), jwt.get("data").toString()));

        if ("y".equalsIgnoreCase(enable_paycmentcheck) && "1".equalsIgnoreCase(req.getParameter("nopayment")) == false && "1".equals(jwt.get("code").toString())) { //jwt code equals mean normal, 2,3 means user had enable 2FA, so skip payment validation here.
            String payment = getPaymentInfo((String) jwt.get("data"), sid);
            log.info(String.format("%s, Find payment %s : (%s)", sid, openId, payment));
            if (StringUtils.isEmpty(payment)) {
                log.warn(String.format("%s, No credit card payment information. Redirect to GoDaddy SSO page through browser.", sid));

                String url_outofwechat;
                JsonObject userInfo = getWeChatInfo(openId);
                if ("en".equalsIgnoreCase(userInfo.getString("language"))) {
                    url_outofwechat = StringUtils.replace(url2browser, "#LANG#", URLEncoder.encode("?lang=en", "UTF-8"));
                } else {
                    url_outofwechat = StringUtils.replace(url2browser, "#LANG#", "");
                }

                resp.sendRedirect(url_outofwechat);
                log.info(String.format("%s, Redirection done cost time ( %s ) ms", sid, (System.currentTimeMillis() - time)));
                return;
            }
        }

        String key = doJWTTransfer((String) jwt.get("data"), sid);
        log.info(String.format("%s, Got %s JWT transfer key: (%s)", sid, openId, key));

        if (StringUtils.isEmpty(key)) {
            redirectToRelogin(resp, sid);
            log.info(String.format("%s, Redirection done cost time ( %s ) ms", sid, (System.currentTimeMillis() - time)));
            return;
        }

        String redirect_url = "https://sso.godaddy.com/login?&app=" + app + "&path=" + URLEncoder.encode(path, "UTF-8") + "&jwt_transfer=" + URLEncoder.encode(key, "UTF-8");
        log.info(String.format("%s, Redirection request to GoDaddy page through: %s ", sid, redirect_url));
        resp.sendRedirect(redirect_url);
        log.info(String.format("%s, Redirection done cost time ( %s ) ms", sid, (System.currentTimeMillis() - time)));

        return;
    }

    private String getPaymentInfo(String jwt, String sid) {
        String paymentAPI = "https://payment.api.godaddy.com/v1/methods?country=CN&currency=USD&includeProfiles=true";

        try {
            HttpRequest req = HttpRequest.get(paymentAPI).header("Content-Type", "application/json").header("Authorization", "sso-jwt " + jwt);
            req.connectionTimeout(gd_api_connect_timeout);
            req.timeout(gd_api_timeout);
            HttpResponse response = req.send();
            response.charset("UTF-8");
            if (response.statusCode() < 300) {
                JsonParser jsonParser = new JsonParser();

                JsonObject map = jsonParser.parseAsJsonObject(response.bodyText());
                JsonArray methods = map.getJsonArray("storedMethods");
                if (methods != null) {
                    for (int i = 0; i < methods.size(); i++) {
                        String category = methods.getJsonObject(i).getString("category");
                        if ("CreditCard".equalsIgnoreCase(category)) {
                            return category;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("%s, Error happend to call JWT API: %s", sid, e.getMessage());
        }

        return "";
    }

    private void redirectToRelogin(HttpServletResponse resp, String sid) throws IOException {
        log.warn(String.format("%s, Login failed. Redirect to GoDaddy SSO page...", sid));
        resp.sendRedirect("https://sso.godaddy.com");
    }


    /**
     * Call wechat redirect API to get temp access code for further processing.
     *
     * @param code
     * @return
     */
    private String getOpenID(String code) {
        String wechatURL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";

        HttpResponse response = HttpRequest.get(wechatURL).send();
        response.charset("UTF-8");
        if (response.statusCode() < 300) {
            JsonParser jsonParser = new JsonParser();
            Map map = jsonParser.parse(response.bodyText());
            return map.containsKey("openid") ? (String) map.get("openid") : "";
        }

        return "";
    }

    /**
     * Call GoDaddy SSO API to ge the JWTToken
     *
     * @param msg
     * @return jwttoken
     */
    private String getGDJWT(OAMsg msg, String sid) {
        return getGDJWT(msg.content, msg.fromUserName, sid);
    }

    private String getGDJWT(String loginStr, String username, String sid) {
        String content = StringUtils.substringAfter(loginStr, "jwt ").trim();
        String[] user_pass = StringUtils.split(content, " ");
        if (user_pass != null && user_pass.length != 2) {
            return "";
        }

        log.info(String.format("%s, Request GoDaddy SSO API to get jwt token for user [%s]", sid, username));
        String body = "{\"username\":\"" + user_pass[0] + "\",\"password\":\"" + user_pass[1] + "\",\"realm\":\"idp\"}";
        try {
            HttpRequest req = HttpRequest.post("https://sso.godaddy.com/v1/api/token").header("Content-Type", "application/json").body(body).open();
            req.connectionTimeout(gd_api_connect_timeout);
            req.timeout(gd_api_timeout);
            HttpResponse response = req.send();
            response.charset("UTF-8");

            if (response.statusCode() < 300) {
                JsonParser jsonParser = new JsonParser();
                Map map = jsonParser.parse(response.bodyText());

                return map.get("data") != null ? (String) map.get("data") : "";
            }
        } catch (Exception e) {
            log.error("%s, Error happend to call JWT API: %s", sid, e.getMessage());
        }

        return "";
    }


    /**
     * Call WeChat user info API to get wechat user informations, including unionid, location, language, gender..etc.
     *
     * @param openid
     * @return
     */
    private JsonObject getWeChatInfo(String openid) {
        String user_info_url = "https://api.weixin.qq.com/cgi-bin/user/info?openid=%s&access_token=%s";
        String token = tf.getToken();
        if (StringUtil.isEmpty(token)) {
            tf.appid = appid;
            tf.appsecret = appsecret;
            tf.refreshToken();
        }
        HttpResponse response = HttpRequest.get(String.format(user_info_url, openid, tf.getToken())).send();
        response.charset("UTF-8");
        JsonParser jsonParser = new JsonParser();
        JsonObject userInfo = jsonParser.parseAsJsonObject(response.bodyText());

        return userInfo;
    }

    /**
     * Get jwt token through OA openID.
     *
     * @param openID
     * @return
     */
    private Map getJWTByOA(String openID, String sid) {
        try {
            log.info(String.format("%s, get JWT token by openid %s", sid, openID));
            HttpRequest req = HttpRequest.post("https://sso.godaddy.com/v1/api/token").
                    form(
                            "client_id", appid,
                            "client_secret", appsecret,
                            "social_site_id", "wechatoa",
                            "user_social_id", openID);

            req.connectionTimeout(gd_api_connect_timeout);
            req.timeout(gd_api_timeout);
            HttpResponse response = req.send();
            response.charset("UTF-8");

            log.info(String.format("%s, JWT API status code: %s", sid, response.statusCode()));

            if (response.statusCode() < 300) {
                JsonParser jsonParser = new JsonParser();
                Map map = jsonParser.parse(response.bodyText());

                return map;
            } else {
                log.error(String.format("%s, Error happend to call JWT API: %s", sid, response.bodyText()));
            }
        } catch (Exception e) {
            log.error("%s, Error happend to call JWT API: %s", sid, e.getMessage());
        }

        return null;
    }

    /**
     * Transfer jwt token to key for redirection.
     * Refer URL: https://confluence.godaddy.com/pages/viewpage.action?spaceKey=AUTH&title=Mobile+App+API
     *
     * @param jwt
     * @return
     */
    private String doJWTTransfer(String jwt, String sid) {
        String transfer_api = "https://sso.godaddy.com/v1/api/token/transfer";
        String auth = "sso-jwt " + jwt;

        try {
            HttpRequest req = HttpRequest.post(transfer_api).header("Content-Type", "application/json").header("Authorization", auth);
            req.connectionTimeout(gd_api_connect_timeout);
            req.timeout(gd_api_timeout);
            HttpResponse response = req.send();

            response.charset("UTF-8");
            JsonParser jsonParser = new JsonParser();

          if (response.statusCode() < 300) {
                Map map = jsonParser.parse(response.bodyText());
                return map.get("data") != null ? (String) ((Map) map.get("data")).get("key") : "";
            }
        } catch (Exception e) {
            log.error("%s, Error happend to call JWT API: %s", sid, e.getMessage());
        }

        return "";
    }

    private void buildOAMsg4FAQ(OAMsg msg, List<FAQ.FAQsBean> matchedFAQs, boolean isMatched, String lang) throws UnsupportedEncodingException {
        if (matchedFAQs.size() == 1) {

            FAQ.FAQsBean bean = matchedFAQs.get(0);
            if ("text".equals(bean.getPhrase_match_return())) {
                //Return message through text format.
                msg.msgType = "text";

                String more_url = StringUtils.replace(FAQ.getFAQByLang(lang).getGodaddy_more_url(), "#KEYWORD#", URLEncoder.encode(msg.content, "UTF-8"));
                String text_answer = StringUtils.replace(bean.getText(), "#godaddy_more_url#", more_url);
                msg.content = text_answer;

            } else {
                //Return message through news format.
                msg.msgType = "news";

                OAMsg.Article article = new OAMsg.Article();
                article.title = bean.getTitle();
                article.desc = bean.getDescription();
                article.picURL = bean.getPic_url();
                article.url = bean.getUrl();

                msg.articles.add(article);
            }
        } else {
            //Return combined text message
            msg.msgType = "text";

            String combine_body;
            if (isMatched) {
                combine_body = FAQ.getFAQByLang(lang).getCombine_faq_templates();
            } else {
                combine_body = FAQ.getFAQByLang(lang).getNomatch_faq_templates();
            }

            StringBuilder faq_body = new StringBuilder();
            for (FAQ.FAQsBean bean : matchedFAQs) {
                String faq_each = FAQ.getFAQByLang(lang).getCombine_faq_each_template();
                faq_each = StringUtils.replace(faq_each, "#TITLE#", bean.getTitle());
                faq_each = StringUtils.replace(faq_each, "#LINK#", bean.getUrl());
                faq_body.append(faq_each);
            }

            combine_body = StringUtils.replace(combine_body, "#FAQ_BODY#", faq_body.toString());
            String more_url = StringUtils.replace(FAQ.getFAQByLang(lang).getGodaddy_more_url(), "#KEYWORD#", URLEncoder.encode(msg.content, "UTF-8"));
            combine_body = StringUtils.replace(combine_body, "#godaddy_more_url#", more_url);

            msg.content = combine_body;
        }
    }

}

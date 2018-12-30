package com.gd.oa;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.io.FileUtil;
import jodd.json.JsonParser;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * This class will refresh the WeChat token hourly.
 */
public class TokenRefresher extends Thread {
    private static final Logger log = LoggerFactory.getLogger(TokenRefresher.class);

    private final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={appsecret}";

    private static final long REFRESH_PERIOD = 3600 * 1000l;

    private String token = "";

    private String token_file = "/mnt/oa_token";

    public String appid = "";

    public String appsecret = "";

    private long latest_ts = System.currentTimeMillis();

    public TokenRefresher() {
        log.info("Start token refresher");
        this.start();
    }


    @Override
    public void run() {
        log.info("Read token from catch.");
        try {
            token = FileUtil.readString(new File(token_file));
            if (StringUtil.isEmpty(token.trim())) {
                throw new IOException("Empty token file");
            }
        } catch (IOException e) {
            log.error("Read token file failed: " + e.getMessage());
            refreshToken();
        }

        while (true) {
            try {
                if (checkToken() == false || System.currentTimeMillis() - latest_ts > REFRESH_PERIOD) {
                    refreshToken();
                }
                Thread.sleep(60 * 1000);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }


    public String refreshToken() {
        try {
            String tokenURL = StringUtil.replace(TOKEN_URL, "{appid}", appid);
            tokenURL = StringUtil.replace(tokenURL, "{appsecret}", appsecret);
            log.info(String.format("Refreshing token now. %s", tokenURL));

            HttpResponse response = HttpRequest.get(tokenURL).send();
            JsonParser jsonParser = new JsonParser();
            Map map = jsonParser.parse(response.body());

            String token_new = (String) map.get("access_token");
            if (StringUtil.isEmpty(token_new) == false) {
                log.info("Got new token: " + token_new);
                token = token_new;
                FileUtil.writeString(new File(token_file), token_new);
                latest_ts = System.currentTimeMillis();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return token;
    }

    public boolean checkToken() {
        String validateURL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + token;
        HttpResponse response = HttpRequest.get(validateURL).send();
        JsonParser jsonParser = new JsonParser();
        Map map = jsonParser.parse(response.body());

        if (map.get("errcode") != null) {
            return false;
        } else {
            return true;
        }
    }


    public String getToken() {
        return token;
    }

}

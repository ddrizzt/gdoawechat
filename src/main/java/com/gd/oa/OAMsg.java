package com.gd.oa;

import com.gd.oa.encrypt.AesException;
import com.gd.oa.encrypt.WXBizMsgCrypt;
import jodd.util.StringUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OAMsg {
    String toUserName;
    public String fromUserName;

    public long createTime;

    public String msgID;

    public String msgType;

    public String event;

    public String content;

    public String mediaID;

    public String thumbMediaID;

    public String title;

    public String description;

    public String recognition;

    public String encrypt_type;

    public String msg_signature;

    public String encrypt;

    public String nonce;

    public String timestamp;


    public List<Article> articles = new ArrayList<Article>();

    public OAMsg() {
    }

    public OAMsg(String toUserName, String fromUserName, long createTime, String content, String msgID, String msgType) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.createTime = createTime;
        this.content = content;
        this.msgID = msgID;
        this.msgType = msgType;
    }

    public OAMsg clone() {
        OAMsg new_OA = new OAMsg(this.toUserName, this.fromUserName, this.createTime, this.content, this.msgID, this.msgType);

        new_OA.mediaID = this.mediaID;
        new_OA.title = this.title;
        new_OA.description = this.description;
        new_OA.thumbMediaID = this.thumbMediaID;
        new_OA.mediaID = this.mediaID;
        new_OA.timestamp = this.timestamp;
        new_OA.articles = this.articles;
        new_OA.encrypt_type = this.encrypt_type;
        new_OA.nonce = this.nonce;
        new_OA.msg_signature = this.msg_signature;

        return new_OA;
    }

    public String toString() {
        if ("aes".equalsIgnoreCase(encrypt_type) && !StringUtil.isEmpty(msg_signature) && !StringUtil.isEmpty(nonce)) {
            return String.format("To: %s, From: %s, TS: %s, CONTENT: %s, MSGID: %s, Type: %s, MEDID: %s, VoiceText: %s, Encrypt: %s", toUserName, fromUserName, createTime, content, msgID, msgType, mediaID, recognition, encrypt);
        } else {
            return String.format("To: %s, From: %s, TS: %s, CONTENT: %s, MSGID: %s, Type: %s, MEDID: %s, VoiceText: %s,", toUserName, fromUserName, createTime, content, msgID, msgType, mediaID, recognition);
        }

    }

    public String toXMLString(WXBizMsgCrypt crypt) throws AesException {
        String result = "";
        if ("aes".equalsIgnoreCase(encrypt_type) && !StringUtil.isEmpty(msg_signature) && !StringUtil.isEmpty(nonce)) {
            result = toEncryptXml(crypt);
        } else {
            result = toRawXML();
        }
        return result;
    }

    private String toEncryptXml(WXBizMsgCrypt crypt) throws AesException {
        String template_text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>" +
                "<MsgSignature><![CDATA[$signature]]></MsgSignature>" +
                "<TimeStamp>$timestamp</TimeStamp>" +
                "<Nonce><![CDATA[$nonce]]></Nonce>" +
                "<Encrypt><![CDATA[$encrypt]]></Encrypt></xml>";

        String[] encrypt_info = crypt.encryptMsg(toRawXML(), this.timestamp, this.nonce);

        String result = StringUtil.replace(template_text, "$timestamp", this.timestamp);
        result = StringUtil.replace(result, "$nonce", this.nonce);
        result = StringUtil.replace(result, "$signature", encrypt_info[0]);
        result = StringUtil.replace(result, "$encrypt", encrypt_info[1]);

        return result;
    }


    private String toRawXML() {
        String template_text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><ToUserName><![CDATA[$toUser]]></ToUserName>" +
                "<FromUserName><![CDATA[$fromUser]]></FromUserName>" +
                "<CreateTime>$createtime</CreateTime>" +
                "<MsgType><![CDATA[text]]></MsgType>" +
                "<Content><![CDATA[$content]]></Content></xml>";

        String template_video = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><ToUserName><![CDATA[$toUser]]></ToUserName>" +
                "<FromUserName><![CDATA[$fromUser]]></FromUserName>" +
                "<CreateTime>$createtime</CreateTime>" +
                "<MsgType><![CDATA[video]]></MsgType>" +
                "<Video><MediaId><![CDATA[$mediaID]]></MediaId>" +
                "<Title><![CDATA[$title]]></Title>" +
                "<Description><![CDATA[$description]]></Description></Video>" +
                "</xml>";

        String template_image = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><ToUserName><![CDATA[$toUser]]></ToUserName>" +
                "<FromUserName><![CDATA[$fromUser]]></FromUserName>" +
                "<CreateTime>$createtime</CreateTime>" +
                "<MsgType><![CDATA[image]]></MsgType>" +
                "<Image><MediaId><![CDATA[$mediaID]]></MediaId></Image>" +
                "</xml>";

        String template_news = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><ToUserName><![CDATA[$toUser]]></ToUserName>" +
                "<FromUserName><![CDATA[$fromUser]]></FromUserName>" +
                "<CreateTime>$createtime</CreateTime>" +
                "<MsgType><![CDATA[news]]></MsgType>" +
                "$articles" +
                "</xml>";

        if (this.msgType.equals("video")) {
            String result = StringUtil.replace(template_video, "$toUser", this.fromUserName);
            result = StringUtil.replace(result, "$fromUser", this.toUserName);
            result = StringUtil.replace(result, "$createtime", Long.toString(this.createTime));
            result = StringUtil.replace(result, "$mediaID", this.mediaID);
            result = StringUtil.replace(result, "$title", this.title);
            result = StringUtil.replace(result, "$description", this.description);

            return result;
        } else if (this.msgType.equals("image")) {
            String result = StringUtil.replace(template_image, "$toUser", this.fromUserName);
            result = StringUtil.replace(result, "$fromUser", this.toUserName);
            result = StringUtil.replace(result, "$createtime", Long.toString(this.createTime));
            result = StringUtil.replace(result, "$mediaID", this.mediaID);

            return result;
        } else if (this.msgType.equals("news")) {
            String result = StringUtil.replace(template_news, "$toUser", this.fromUserName);
            result = StringUtil.replace(result, "$fromUser", this.toUserName);
            result = StringUtil.replace(result, "$createtime", Long.toString(this.createTime));
            result = StringUtil.replace(result, "$articles", Article.toXML(this.articles));

            return result;
        } else {
            String result = StringUtil.replace(template_text, "$toUser", this.fromUserName);
            result = StringUtil.replace(result, "$fromUser", this.toUserName);
            result = StringUtil.replace(result, "$createtime", Long.toString(this.createTime));
            result = StringUtil.replace(result, "$content", this.content);

            return result;
        }
    }


    public static OAMsg toOAMsg(InputStream inputStream, String encrypt_type, String msg_signature, String timestamp, String nonce, WXBizMsgCrypt wxMsgCrypt) {
        SAXBuilder builder = new SAXBuilder();

        try {
            Document document = builder.build(inputStream);

            String encryptStr = "";
            if ("aes".equalsIgnoreCase(encrypt_type) && !StringUtil.isEmpty(msg_signature) && !StringUtil.isEmpty(nonce) && wxMsgCrypt != null) { //decrypt the string first.
                Element root = document.getRootElement();
                Element encrypt = root.getChild("Encrypt");
                if (encrypt != null && !StringUtil.isEmpty(encrypt.getText())) {
                    encryptStr = encrypt.getText();
                }
                String decryptMsg = wxMsgCrypt.decryptMsg(msg_signature, timestamp, nonce, encryptStr);
                document = builder.build(new ByteArrayInputStream(decryptMsg.getBytes()));
            }

            Element root = document.getRootElement();
            Element toUserName = root.getChild("ToUserName");
            String toUsernameStr = toUserName != null ? toUserName.getText().trim() : "";
            Element fromUserName = root.getChild("FromUserName");
            String fromUserNameStr = fromUserName != null ? fromUserName.getText().trim() : "";
            Element createTime = root.getChild("CreateTime");
            String createTimeStr = createTime != null ? createTime.getText().trim() : "";
            Element content = root.getChild("Content");
            String contentStr = content != null ? content.getText().trim() : "";
            Element msgType = root.getChild("MsgType");
            String msgTypeStr = msgType != null ? msgType.getText().trim() : "";
            Element msgId = root.getChild("MsgId");
            String msgIdStr = msgId != null ? msgId.getText().trim() : "";
            Element mediaId = root.getChild("MediaId");
            String mediaIdStr = mediaId != null ? mediaId.getText().trim() : "";
            Element recognition = root.getChild("Recognition");
            String recognitionStr = recognition != null ? recognition.getText().trim() : "";

            OAMsg msg = new OAMsg(toUsernameStr, fromUserNameStr, Long.parseLong(createTimeStr), contentStr, msgIdStr, msgTypeStr);
            msg.mediaID = mediaIdStr;
            msg.recognition = recognitionStr;
            msg.encrypt_type = encrypt_type;
            msg.msg_signature = msg_signature;
            msg.encrypt = encryptStr;
            msg.nonce = nonce;
            msg.timestamp = timestamp;

            Element event = root.getChild("Event");
            msg.event = event != null ? event.getText().trim() : "";

            return msg;
        } catch (Exception e) {
            return null;
        }
    }

    public static class Article {
        public String title;
        public String desc;
        public String picURL;
        public String url;

        public Article() {
        }

        public String toString() {
            return String.format("%s, %s, %s, %s", title, desc, picURL, url);
        }

        public String toXML() {
            String template_article = "<item><Title><![CDATA[$title]]>" +
                    "</Title>" +
                    "<Description><![CDATA[$desc]]>" +
                    "</Description>" +
                    "<PicUrl><![CDATA[$picurl]]>" +
                    "</PicUrl>" +
                    "<Url><![CDATA[$url]]>" +
                    "</Url></item>";

            String result = StringUtil.replace(template_article, "$title", this.title);
            result = StringUtil.replace(result, "$desc", this.desc);
            result = StringUtil.replace(result, "$picurl", this.picURL);
            result = StringUtil.replace(result, "$url", this.url);
            return result;
        }

        public static String toXML(List<Article> articles) {
            StringBuilder articles_body = new StringBuilder();

            for (Article a : articles) {
                articles_body.append(a.toXML());
            }

            if (articles_body.length() > 0) {
                return "<ArticleCount>" + articles.size() + "</ArticleCount><Articles>" + articles_body.toString() + "</Articles>";
            }
            return "";
        }
    }

}

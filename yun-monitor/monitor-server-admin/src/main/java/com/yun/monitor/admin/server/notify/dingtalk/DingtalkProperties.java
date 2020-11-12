package com.yun.monitor.admin.server.notify.dingtalk;


import com.yun.monitor.admin.server.notify.dingtalk.message.MsgType;

public final class DingtalkProperties {
    // 钉钉接口访问token
    private String webhookToken;
    private MsgType msgtype;
    // 安全设置，消息中包含该关键词才可以发送成功
    private String keywords;
    // 消息标题， MsgType.Link 和 MsgType.Markdown 使用
    private String title;
    // 消息内容
    private String content;
    // 被@人的手机号， MsgType.Text 和 MsgType.Markdown 使用
    private String atMobiles;
    // 点击消息跳转的URL，MsgType.Link 使用
    private String picUrl;
    // 图片URL，MsgType.Link 使用
    private String messageUrl;

    public String getWebhookToken() {
        return webhookToken;
    }

    public void setWebhookToken(String webhookToken) {
        this.webhookToken = webhookToken;
    }

    public MsgType getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(MsgType msgtype) {
        this.msgtype = msgtype;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public String getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(String atMobiles) {
        this.atMobiles = atMobiles;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public DingtalkProperties setContent(String content) {
        this.content = content;
        return this;
    }
}

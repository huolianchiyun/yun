package com.yun.monitor.admin.server.notify.dingtalk.message;

import com.yun.monitor.admin.server.notify.dingtalk.DingtalkProperties;
import com.yun.monitor.admin.server.notify.dingtalk.MessageBuilder;

public class LinkMessage extends Message {
    private Link link;

    private LinkMessage() {
        super(MsgType.Link);
    }

    public Link getLink() {
        return link;
    }

    static class Link {
        // 消息标题
        private String title;
        // 消息内容, 如果太长只会部分展示
        private String text;
        //点击消息跳转的URL
        private String messageUrl;
        // 图片URL
        private String picUrl;

        private Link(String title, String text, String messageUrl, String picUrl) {
            this.title = title;
            this.text = text;
            this.messageUrl = messageUrl;
            this.picUrl = picUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }

        public String getMessageUrl() {
            return messageUrl;
        }

        public String getPicUrl() {
            return picUrl;
        }
    }

    public static class LinkMessageBuilder implements MessageBuilder {
        private LinkMessage linkMessage;

        private LinkMessageBuilder() {
            this.linkMessage = new LinkMessage();
        }

        @Override
        public MessageBuilder resource(DingtalkProperties resource) {
            linkMessage.link = new Link(resource.getTitle(),
                    String.format("%s\n%s", resource.getKeywords(), resource.getContent()),
                    resource.getMessageUrl(), resource.getPicUrl());
            return this;
        }

        @Override
        public LinkMessage build() {
            return linkMessage;
        }
    }
}

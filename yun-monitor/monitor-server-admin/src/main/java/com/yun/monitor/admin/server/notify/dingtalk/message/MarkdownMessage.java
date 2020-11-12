package com.yun.monitor.admin.server.notify.dingtalk.message;

import com.yun.monitor.admin.server.notify.dingtalk.DingtalkProperties;
import com.yun.monitor.admin.server.notify.dingtalk.MessageBuilder;
import org.springframework.util.StringUtils;
import java.util.Arrays;

public class MarkdownMessage extends Message {
    private Markdown markdown;
    private At at;

    private MarkdownMessage() {
        super(MsgType.MarkDown);
    }

    public Markdown getMarkdown() {
        return markdown;
    }

    public At getAt() {
        return at;
    }

    static class Markdown {
        // 首屏会话透出的展示内容
        private String title;
        // markdown格式的消息
        private String text;

        private Markdown(String title, String text) {
            this.title = title;
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }
    }

    public static class MarkdownMessageBuilder implements MessageBuilder {
        private MarkdownMessage markdownMessage;

        private MarkdownMessageBuilder() {
            this.markdownMessage = new MarkdownMessage();
        }

        /**
         * 构建消息
         * @param title 消息标题
         * @param keywords 安全关键字 消息中包含关键词才可以发送成功。例如：添加了一个自定义关键词：监控报警
         * @param content  消息内容
         * @return MarkdownMessageBuilder
         */
        private MarkdownMessageBuilder markdown(String title, String keywords, String content) {
            markdownMessage.markdown = new Markdown(title, String.format("%s\n%s", keywords, content));
            return this;
        }

        /**
         * 构建 @人
         *
         * @param atMobiles 被@人的手机号（在content里添加@人的手机号）
         * @return MarkdownMessageBuilder
         */
        private MarkdownMessageBuilder at(String... atMobiles) {
            markdownMessage.at = new At();
            markdownMessage.at.setAtMobiles(Arrays.asList(atMobiles));
            return this;
        }

        @Override
        public MessageBuilder resource(DingtalkProperties resource) {
            this.markdown(resource.getTitle(), resource.getKeywords(), resource.getContent())
                    .at(StringUtils.hasText(resource.getAtMobiles()) ? resource.getAtMobiles().split(",") : new String[0]);
            return this;
        }

        @Override
        public MarkdownMessage build() {
            return markdownMessage;
        }
    }
}

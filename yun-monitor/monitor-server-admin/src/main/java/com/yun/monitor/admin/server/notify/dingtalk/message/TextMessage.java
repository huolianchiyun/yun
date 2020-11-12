package com.yun.monitor.admin.server.notify.dingtalk.message;

import com.yun.monitor.admin.server.notify.dingtalk.MessageBuilder;
import com.yun.monitor.admin.server.notify.dingtalk.DingtalkProperties;
import org.springframework.util.StringUtils;
import java.util.Arrays;

public class TextMessage extends Message {
    private Text text;
    private At at;

    private TextMessage() {
        super(MsgType.Text);
    }

    public Text getText() {
        return text;
    }

    public At getAt() {
        return at;
    }

    public static class TextMessageBuilder implements MessageBuilder {
        private TextMessage textMessage;

        private TextMessageBuilder() {
            this.textMessage = new TextMessage();
        }

        /**
         * 构建消息
         *
         * @param keywords 安全关键字 消息中包含关键词才可以发送成功。例如：添加了一个自定义关键词：监控报警
         * @param content  消息内容
         * @return TextMessageBuilder
         */
        private TextMessageBuilder text(String keywords, String content) {
            textMessage.text = new Text(String.format("%s\n%s", keywords, content));
            return this;
        }

        /**
         * 构建 @人
         *
         * @param atMobiles 被@人的手机号（在content里添加@人的手机号）
         * @return TextMessageBuilder
         */
        private TextMessageBuilder at(String... atMobiles) {
            textMessage.at = new At();
            textMessage.at.setAtMobiles(Arrays.asList(atMobiles));
            return this;
        }

        @Override
        public MessageBuilder resource(DingtalkProperties resource) {
            this.text(resource.getKeywords(), resource.getContent())
                    .at(StringUtils.hasText(resource.getAtMobiles())? resource.getAtMobiles().split(",") : new String[0]);
            return this;
        }

        @Override
        public TextMessage build() {
            return textMessage;
        }
    }

    static class Text {
        private String content;

        public Text(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}

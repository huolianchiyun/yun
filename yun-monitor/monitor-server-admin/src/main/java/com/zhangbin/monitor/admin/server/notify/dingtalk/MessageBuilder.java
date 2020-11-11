package com.zhangbin.monitor.admin.server.notify.dingtalk;

import com.zhangbin.monitor.admin.server.notify.dingtalk.message.Message;

public interface MessageBuilder {
    /**
     * 设置消息构建所需资源
     *
     * @param resource 资源
     * @return MessageBuilder
     */
    MessageBuilder resource(DingtalkProperties resource);

    /**
     * 构建消息
     *
     * @return Message
     */
    Message build();
}

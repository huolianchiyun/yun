package com.yun.monitor.admin.server.notify.dingtalk;

import com.yun.monitor.admin.server.notify.dingtalk.message.MsgType;
import com.yun.monitor.admin.server.notify.dingtalk.message.TextMessage;
import com.yun.monitor.admin.server.notify.dingtalk.message.LinkMessage;
import com.yun.monitor.admin.server.notify.dingtalk.message.MarkdownMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class MessageFactory {
    // lazy load
    private static Map<MsgType, Class<? extends MessageBuilder>> builderMap = BuilderHolder.builderMap();

    static class BuilderHolder {
        static Map<MsgType, Class<? extends MessageBuilder>> builderMap() {
            Map<MsgType, Class<? extends MessageBuilder>> builderMap = new HashMap<>();
            builderMap.put(MsgType.Text, TextMessage.TextMessageBuilder.class);
            builderMap.put(MsgType.MarkDown, MarkdownMessage.MarkdownMessageBuilder.class);
            builderMap.put(MsgType.Link, LinkMessage.LinkMessageBuilder.class);
            return builderMap;
        }
    }

    /**
     * 根据消息类型获取响应的消息构建器
     *
     * @param msgType 消息类型
     * @return MessageBuilder
     */
    public static MessageBuilder getBuilderBy(MsgType msgType) {
        try {
            final Class<? extends MessageBuilder> builderClass = builderMap.get(msgType);
            final Constructor<? extends MessageBuilder> noArgConstruct = builderClass.getDeclaredConstructor();
            noArgConstruct.setAccessible(true);
            return noArgConstruct.newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

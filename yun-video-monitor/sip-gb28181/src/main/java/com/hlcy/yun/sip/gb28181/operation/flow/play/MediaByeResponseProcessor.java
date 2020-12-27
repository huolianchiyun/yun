package com.hlcy.yun.sip.gb28181.operation.flow.play;

import com.hlcy.yun.sip.gb28181.operation.ResponseProcessor;
import com.hlcy.yun.sip.gb28181.operation.flow.FlowContext;

import javax.sip.ResponseEvent;

/**
 * 客户端主动发起的实时视音频点播流程: 16->17 or 18->19<br/>
 * <P>
 * 16:媒体服务器收到BYE消息后回复200OK响应,会话断开。<br/>
 * 17:SIP服务器向媒体服务器发送BYE消息,断开消息2、3、6建立的同媒体服务器的Invite会话。<br/>
 * 18:媒体服务器收到BYE消息后回复200OK响应,会话断开。<br/>
 * 19:SIP服务器向媒体流发送者发送BYE消息,断开消息4、5、7建立的同媒体流发送者的Invite会话。<br/>
 * </P>
 */
public class MediaByeResponseProcessor extends ResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {

    }
}

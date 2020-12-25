package com.hlcy.yun.sip.gb28181.service;

import com.hlcy.yun.sip.gb28181.bean.Device;
import com.hlcy.yun.sip.gb28181.config.GB28181Properties;
import com.hlcy.yun.sip.gb28181.message.factory.SipRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.sip.message.Request;
import static com.hlcy.yun.sip.gb28181.client.RequestSender.sendRequest;

@Service
@RequiredArgsConstructor
public class DefaultPlayer implements Player {
    private final GB28181Properties properties;


    @Override
    public void play(Device device) {
        // a)1:媒体流接收者向SIP服务器发送Invite消息,消息头域中携带Subject字段,表明点播的视频源ID、发送方媒体流序列号、媒体流接收者ID、接收端媒体流序列号等参数,SDP消息体中s字段为“Play”代表实时点播。19GB/T28181—2016
        // b)2:SIP服务器收到Invite请求后,通过三方呼叫控制建立媒体服务器和媒体流发送者之间的媒体连接。向媒体服务器发送Invite消息,此消息不携带SDP消息体。
//        final Request inviteMedia = SipRequestFactory.getInviteRequest(device);
//        sendRequest(inviteMedia);
        // c)3:媒体服务器收到SIP服务器的Invite请求后,回复200OK响应,携带SDP消息体,消息体中描述了媒体服务器接收媒体流的IP、端口、媒体格式等内容。



        // d)4:SIP服务器收到媒体服务器返回的200OK响应后,向媒体流发送者发送Invite请求,请求中携带消息3中媒体服务器回复的200OK响应消息体,s字段为“Play”代表实时点播,增加y字段描述SSRC值,f字段描述媒体参数。
//        final Request inviteDevice = SipRequestFactory.getInviteRequest(device);
//        sendRequest(inviteDevice);


        // e)5:媒体流发送者收到SIP服务器的Invite请求后,回复200OK响应,携带SDP消息体,消息体中描述了媒体流发送者发送媒体流的IP、端口、媒体格式、SSRC字段等内容。
        // f)6:SIP服务器收到媒体流发送者返回的200OK响应后,向媒体服务器发送ACK请求,请求中携带消息5中媒体流发送者回复的200OK响应消息体,完成与媒体服务器的Invite会话建立过程。


        // g)7:SIP服务器收到媒体流发送者返回的200OK响应后,向媒体流发送者发送ACK请求,请求中不携带消息体,完成与媒体流发送者的Invite会话建立过程。
        // h)8:完成三方呼叫控制后,SIP服务器通过B2BUA代理方式建立媒体流接收者和媒体服务器之间的媒体连接。在消息1中增加SSRC值,转发给媒体服务器。
        // i)9:媒体服务器收到Invite请求,回复200OK响应,携带SDP消息体,消息体中描述了媒体服务器发送媒体流的IP、端口、媒体格式、SSRC值等内容。
        // j)10:SIP服务器将消息9转发给媒体流接收者。
        // k)11:媒体流接收者收到200OK响应后,回复ACK消息,完成与SIP服务器的Invite会话建立过程。
        // l)12:SIP服务器将消息11转发给媒体服务器,完成与媒体服务器的Invite会话建立过程。

    }

    @Override
    public void stop(String ssrc) {
        // m)13:媒体流接收者向SIP服务器发送BYE消息,断开消息1、10、11建立的同媒体流接收者的Invite会话。
        // n)14:SIP服务器收到BYE消息后回复200OK响应,会话断开。
        // o)15:SIP服务器收到BYE消息后向媒体服务器发送BYE消息,断开消息8、9、12建立的同媒体服务器的Invite会话。
        // p)16:媒体服务器收到BYE消息后回复200OK响应,会话断开。
        // q)17:SIP服务器向媒体服务器发送BYE消息,断开消息2、3、6建立的同媒体服务器的Invite会话。
        // r)18:媒体服务器收到BYE消息后回复200OK响应,会话断开。
        // s)19:SIP服务器向媒体流发送者发送BYE消息,断开消息4、5、7建立的同媒体流发送者的Invite会话。
        // t)20:媒体流发送者收到BYE消息后回复200OK响应,会话断开。
    }
}

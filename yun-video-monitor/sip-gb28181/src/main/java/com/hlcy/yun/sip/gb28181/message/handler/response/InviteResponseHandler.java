package com.hlcy.yun.sip.gb28181.message.handler.response;

import com.hlcy.yun.sip.gb28181.message.ResponseHandler;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class InviteResponseHandler extends ResponseHandler {
    @Override
    protected void handle(ResponseEvent evt) {
        try {
            Response response = evt.getResponse();
            int statusCode = response.getStatusCode();

            // trying不会回复
            if (statusCode == Response.TRYING) {
            }
            // 成功响应
            // 下发ack
            if (statusCode == Response.OK) {
                // ClientTransaction clientTransaction = evt.getClientTransaction();
                // if(clientTransaction == null){
                // logger.error("回复ACK时，clientTransaction为null >>> {}",response);
                // return;
                // }
                // Dialog clientDialog = clientTransaction.getDialog();

                // CSeqHeader clientCSeqHeader = (CSeqHeader)
                // response.getHeader(CSeqHeader.NAME);
                // long cseqId = clientCSeqHeader.getSeqNumber();
                // /*
                // createAck函数，创建的ackRequest，会采用Invite响应的200OK，中的contact字段中的地址，作为目标地址。
                // 有的终端传上来的可能还是内网地址，会造成ack发送不出去。接受不到音视频流
                // 所以在此处统一替换地址。和响应消息的Via头中的地址保持一致。
                // */
                // Request ackRequest = clientDialog.createAck(cseqId);
                // SipURI requestURI = (SipURI) ackRequest.getRequestURI();
                // ViaHeader viaHeader = (ViaHeader) response.getHeader(ViaHeader.NAME);
                // try {
                // requestURI.setHost(viaHeader.getHost());
                // } catch (Exception e) {
                // e.printStackTrace();
                // }
                // requestURI.setPort(viaHeader.getPort());
                // clientDialog.sendAck(ackRequest);

                Dialog dialog = evt.getDialog();
                CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
                Request reqAck = dialog.createAck(cseq.getSeqNumber());

                SipURI requestURI = (SipURI) reqAck.getRequestURI();
                ViaHeader viaHeader = (ViaHeader) response.getHeader(ViaHeader.NAME);
                // String viaHost =viaHeader.getHost();
                //getHost()函数取回的IP地址是“[xxx.xxx.xxx.xxx:yyyy]”的格式，需用正则表达式截取为“xxx.xxx.xxx.xxx"格式
                // Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
                // Matcher matcher = p.matcher(viaHeader.getHost());
                // if (matcher.find()) {
                // 	requestURI.setHost(matcher.group());
                // }
//                requestURI.setHost(viaHeader.getHost());
                requestURI.setPort(viaHeader.getPort());
                reqAck.setRequestURI(requestURI);
                dialog.sendAck(reqAck);
            }
        } catch (InvalidArgumentException | SipException e) {
            e.printStackTrace();
        }
    }
}

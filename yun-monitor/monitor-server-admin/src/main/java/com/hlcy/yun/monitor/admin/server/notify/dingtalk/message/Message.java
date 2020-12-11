package com.hlcy.yun.monitor.admin.server.notify.dingtalk.message;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public abstract class Message {
   private MsgType msgtype;

   public Message(MsgType msgtype) {
      this.msgtype = msgtype;
   }

   public MsgType getMsgtype() {
      return msgtype;
   }

   public String toJsonString(){
      return JSONObject.toJSONString(this, SerializerFeature.WriteEnumUsingToString);
   }
}

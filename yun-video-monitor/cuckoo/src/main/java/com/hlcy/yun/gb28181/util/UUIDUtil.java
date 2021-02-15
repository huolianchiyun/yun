package com.hlcy.yun.gb28181.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class UUIDUtil {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(getUUID());
       String ss=  "v=0\n" +
               "o=64010000002020000001 0 0 IN IP4 172.18.16.3\n" +
               "s=# #ms20091214\n" +
               "c=IN IP4 172.18.16.3\n" +
               "t=00\n" +
               "m=video 6000 RTP/AVP 96 98 97\n" +
               "a=recvonly\n" +
               "a=rtpmap:96 PS/90000\n" +
               "a=rtpmap:98 H264/90000\n" +
               "a=rtpmap:97 MPEG4/90000";
        System.out.println(ss.getBytes("utf-8").length);
    }

    public static String getUUID(){
     return UUID.randomUUID().toString().replace("-","");
    }
}

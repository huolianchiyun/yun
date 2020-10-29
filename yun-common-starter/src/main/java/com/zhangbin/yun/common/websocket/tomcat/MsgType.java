package com.zhangbin.yun.common.websocket.tomcat;

public enum MsgType {
	/** 连接 */
	CONNECT,
	/** 关闭 */
	CLOSE,
	/** 信息 */
	INFO,
	/** 错误 */
	ERROR
}

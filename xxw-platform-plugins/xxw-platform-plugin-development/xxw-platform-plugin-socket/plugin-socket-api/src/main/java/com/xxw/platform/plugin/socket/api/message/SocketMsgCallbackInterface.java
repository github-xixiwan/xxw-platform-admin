package com.xxw.platform.plugin.socket.api.message;

import com.xxw.platform.plugin.socket.api.session.pojo.SocketSession;

/**
 * Socket消息接收回调接口
 *
 * @author liaoxiting
 * @date 2021/6/2 上午9:53
 */
@FunctionalInterface
public interface SocketMsgCallbackInterface {

    /**
     * 收到消息的回调
     *
     * @param msgType       消息类型
     * @param msg           消息体
     * @param socketSession 本次通信的会话
     * @author liaoxiting
     * @date 2021/6/2 上午9:51
     **/
    void callback(String msgType, Object msg, SocketSession socketSession);
}

package com.xxw.platform.plugin.socket.business.websocket.server;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.xxw.platform.plugin.jwt.api.context.JwtContext;
import com.xxw.platform.plugin.jwt.api.pojo.payload.DefaultJwtPayload;
import com.xxw.platform.plugin.socket.api.enums.ClientMessageTypeEnum;
import com.xxw.platform.plugin.socket.api.enums.ServerMessageTypeEnum;
import com.xxw.platform.plugin.socket.api.enums.SystemMessageTypeEnum;
import com.xxw.platform.plugin.socket.api.message.SocketMsgCallbackInterface;
import com.xxw.platform.plugin.socket.api.session.pojo.SocketSession;
import com.xxw.platform.plugin.socket.business.websocket.message.SocketMessageCenter;
import com.xxw.platform.plugin.socket.business.websocket.operator.channel.SocketOperator;
import com.xxw.platform.plugin.socket.business.websocket.pojo.WebSocketMessageDTO;
import com.xxw.platform.plugin.socket.business.websocket.session.SessionCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * 消息监听处理器
 *
 * @author liaoxiting
 * @date 2021/6/1 下午2:35
 */
@Slf4j
@ServerEndpoint(value = "/webSocket/{token}")
@Component
public class WebSocketServer {

    /**
     * 连接建立调用的方法
     * <p>
     * 暂时无用，需要在建立连接的时候做一些事情的话可以修改这里
     *
     * @param session 会话信息
     * @author liaoxiting
     * @date 2021/6/21 下午5:14
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        String userId = null;
        try {
            // 解析用户信息
            DefaultJwtPayload defaultPayload = JwtContext.me().getDefaultPayload(token);
            userId = defaultPayload.getUserId().toString();
        } catch (io.jsonwebtoken.JwtException e) {
            try {
                session.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        // 操作api包装
        SocketOperator SocketOperator = new SocketOperator(session);

        // 回复消息
        WebSocketMessageDTO replyMsg = new WebSocketMessageDTO();
        replyMsg.setServerMsgType(ServerMessageTypeEnum.SYS_REPLY_MSG_TYPE.getCode());
        replyMsg.setToUserId(userId);

        // 创建会话对象
        SocketSession<SocketOperator> socketSession = new SocketSession<>();
        try {
            // 设置回复内容
            replyMsg.setData(session.getId());
            socketSession.setSessionId(session.getId());
            socketSession.setUserId(userId);
            socketSession.setSocketOperatorApi(SocketOperator);
            socketSession.setToken(token);
            socketSession.setConnectionTime(System.currentTimeMillis());

            // 维护会话
            SessionCenter.addSocketSession(socketSession);
        } finally {
            // 回复消息
            SocketOperator.writeAndFlush(replyMsg);

            // 触发首次连接回调
            SocketMsgCallbackInterface socketMsgCallbackInterface = SocketMessageCenter.getSocketMsgCallbackInterface(SystemMessageTypeEnum.SYS_LISTENER_ONOPEN.getCode());
            if (ObjectUtil.isNotEmpty(socketMsgCallbackInterface)) {
                // 触发回调
                socketMsgCallbackInterface.callback(SystemMessageTypeEnum.SYS_LISTENER_ONOPEN.getCode(), null, socketSession);
            }
        }

    }

    /**
     * 连接关闭调用的方法
     *
     * @param session 会话信息
     * @author liaoxiting
     * @date 2021/6/21 下午5:14
     **/
    @OnClose
    public void onClose(Session session) {
        try {
            SocketSession<SocketOperator> socketSession = SessionCenter.getSessionBySessionId(session.getId());
            // 触发首次连接回调
            SocketMsgCallbackInterface socketMsgCallbackInterface = SocketMessageCenter.getSocketMsgCallbackInterface(SystemMessageTypeEnum.SYS_LISTENER_ONCLOSE.getCode());
            if (ObjectUtil.isNotEmpty(socketSession) && ObjectUtil.isNotEmpty(socketMsgCallbackInterface)) {
                // 触发回调
                socketMsgCallbackInterface.callback(SystemMessageTypeEnum.SYS_LISTENER_ONCLOSE.getCode(), null, socketSession);
            }
        } finally {
            SessionCenter.deleteById(session.getId());
        }
    }

    /**
     * 收到消息调用的方法
     *
     * @param message       　接收到的消息
     * @param socketChannel 会话信息
     * @author liaoxiting
     * @date 2021/6/21 下午5:14
     **/
    @OnMessage
    public void onMessage(String message, Session socketChannel) {

        // 转换为Java对象
        WebSocketMessageDTO WebSocketMessageDTO = JSON.parseObject(message, WebSocketMessageDTO.class);

        // 维护通道是否已初始化
        SocketSession<SocketOperator> socketSession = SessionCenter.getSessionBySessionId(socketChannel.getId());

        // 心跳包
        if (ObjectUtil.isNotEmpty(socketSession) && ClientMessageTypeEnum.USER_HEART.getCode().equals(WebSocketMessageDTO.getClientMsgType())) {
            // 更新会话最后活跃时间
            if (ObjectUtil.isNotEmpty(socketSession)) {
                socketSession.setLastActiveTime(System.currentTimeMillis());
            }
            return;
        }

        // 用户ID为空不处理直接跳过
        if (ObjectUtil.isEmpty(WebSocketMessageDTO.getFormUserId())) {
            return;
        }

        // 会话建立成功执行业务逻辑
        if (ObjectUtil.isNotEmpty(socketSession)) {

            // 更新最后会话时间
            socketSession.setLastActiveTime(System.currentTimeMillis());

            // 找到该消息的处理器
            SocketMsgCallbackInterface socketMsgCallbackInterface = SocketMessageCenter.getSocketMsgCallbackInterface(WebSocketMessageDTO.getClientMsgType());
            if (ObjectUtil.isNotEmpty(socketMsgCallbackInterface)) {
                // 触发回调
                socketMsgCallbackInterface.callback(WebSocketMessageDTO.getClientMsgType(), WebSocketMessageDTO, socketSession);
            } else {
                socketChannel.getAsyncRemote().sendText("{\"serverMsgType\":\"404\"}");
            }
        }
    }

    /**
     * 会话发送异常调用的方法
     *
     * @param session 会话信息
     * @param error   　错误信息
     * @author liaoxiting
     * @date 2021/6/21 下午5:14
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        SocketSession<SocketOperator> socketSession = SessionCenter.getSessionBySessionId(session.getId());
        // 触发首次连接回调
        SocketMsgCallbackInterface socketMsgCallbackInterface = SocketMessageCenter.getSocketMsgCallbackInterface(SystemMessageTypeEnum.SYS_LISTENER_ONERROR.getCode());
        if (ObjectUtil.isNotEmpty(socketMsgCallbackInterface)) {
            // 触发回调
            socketMsgCallbackInterface.callback(SystemMessageTypeEnum.SYS_LISTENER_ONERROR.getCode(), error, socketSession);
        }
    }
}

package com.xxw.platform.plugin.socket.api.session;

/**
 * socket会话操作接口
 * <p>
 * 该接口面向会话，须基于会话的通道调用。
 * 该接口支持扩展，可参考WebSocket模块中{@link import com.xxw.platform.plugin.socket.business.websocket.operator.channel}包下的类
 *
 * @author liaoxiting
 * @date 2021/6/1 上午11:46
 */
public interface SocketSessionOperatorApi {

    /**
     * 写出数据，经过责任链
     *
     * @author liaoxiting
     * @date 2021/6/1 上午11:48
     **/
    void writeAndFlush(Object obj);

    /**
     * 写出数据，不经过责任链
     *
     * @author liaoxiting
     * @date 2021/6/1 上午11:48
     **/
    void writeToChannel(Object obj);

    /**
     * 关闭会话
     *
     * @author liaoxiting
     * @date 2021/6/1 上午11:48
     **/
    void close();

    /**
     * 是否存活
     *
     * @return {@link boolean}
     * @author liaoxiting
     * @date 2021/6/1 上午11:50
     **/
    boolean isInvalid();
}

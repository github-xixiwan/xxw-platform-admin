package com.xxw.platform.plugin.socket.starter;

import com.xxw.platform.plugin.socket.api.SocketOperatorApi;
import com.xxw.platform.plugin.socket.business.websocket.operator.WebSocketOperator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Socket的自动配置类
 *
 * @author liaoxiting
 * @date 2021/6/2 下午5:48
 */
@Configuration
public class SocketAutoConfiguration {

    /**
     * Socket操作实现类
     *
     * @author liaoxiting
     * @date 2021/6/2 下午5:48
     **/
    @Bean
    @ConditionalOnMissingBean(SocketOperatorApi.class)
    public SocketOperatorApi socketOperatorApi() {
        return new WebSocketOperator();
    }

}

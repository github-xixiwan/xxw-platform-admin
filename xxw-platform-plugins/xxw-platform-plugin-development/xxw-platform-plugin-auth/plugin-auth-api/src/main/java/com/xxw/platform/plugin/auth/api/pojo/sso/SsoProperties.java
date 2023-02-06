package com.xxw.platform.plugin.auth.api.pojo.sso;

import com.xxw.platform.plugin.auth.api.enums.SsoClientTypeEnum;
import lombok.Data;

/**
 * SSO的配置
 *
 * @author liaoxiting
 * @date 2021/5/25 22:28
 */
@Data
public class SsoProperties {

    /**
     * sso服务端还是客户端（传server或者client）
     */
    private String ssoClientType = SsoClientTypeEnum.client.name();

    /**
     * 是否开启，true-开启单点，false-关闭单点
     */
    private Boolean openFlag;

}

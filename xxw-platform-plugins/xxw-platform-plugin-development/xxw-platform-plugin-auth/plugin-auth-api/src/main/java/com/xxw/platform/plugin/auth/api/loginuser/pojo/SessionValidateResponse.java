package com.xxw.platform.plugin.auth.api.loginuser.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Session校验
 *
 * @author liaoxiting
 * @date 2021/9/29 11:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionValidateResponse {

    /**
     * 校验结果
     */
    private Boolean validateResult;

}

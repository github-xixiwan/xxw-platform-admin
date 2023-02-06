package com.xxw.platform.plugin.scanner.api.pojo.resource;

import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取用户资源请求
 *
 * @author liaoxiting
 * @date 2020/10/19 22:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserResourceParam extends BaseRequest {

    /**
     * 用户id
     */
    private String userId;

}

package com.xxw.platform.plugin.scanner.api.pojo.resource;

import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取资源通过url请求
 *
 * @author liaoxiting
 * @date 2020/10/19 22:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceUrlParam extends BaseRequest {

    private String url;

}

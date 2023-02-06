package com.xxw.platform.plugin.config.business.pojo;

import lombok.Data;

import java.util.List;

/**
 * 初始化参数响应
 *
 * @author liaoxiting
 * @date 2022/10/24 14:59
 */
@Data
public class InitConfigResponse {

    /**
     * 初始化参数界面的整体标题
     */
    public String title;

    /**
     * 初始化参数界面的详情信息
     */
    public String description;

    /**
     * 具体的参数分组列表，初始化界面可以返回多个分组
     */
    public List<InitConfigGroup> initConfigGroupList;

}

package com.xxw.platform.plugin.config.business.pojo;

import com.xxw.platform.plugin.config.api.pojo.ConfigInitItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 初始化界面参数的分组
 *
 * @author liaoxiting
 * @date 2022/10/24 15:01
 */
@Data
@AllArgsConstructor
public class InitConfigGroup {

    /**
     * 参数的分组标题
     */
    private String title;

    /**
     * 分组的详情
     */
    private String description;

    /**
     * 本分组下，初始化的列表
     */
    private List<ConfigInitItem> configInitItemList;

}

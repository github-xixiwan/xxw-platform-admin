package com.xxw.platform.plugin.expand.api;

import com.xxw.platform.plugin.expand.api.pojo.ExpandDataInfo;
import com.xxw.platform.plugin.expand.api.pojo.ExpandFieldInfo;

import java.util.List;
import java.util.Map;

/**
 * 拓展字段Api
 *
 * @author liaoxiting
 * @date 2022-03-29 23:14:31
 */
public interface ExpandApi {

    /**
     * 保存或更新动态数据
     *
     * @author liaoxiting
     * @date 2022/3/31 21:20
     */
    void saveOrUpdateExpandData(ExpandDataInfo expandDataInfo);

    /**
     * 获取列表需要的拓展字段信息
     *
     * @author liaoxiting
     * @date 2022/4/1 9:48
     */
    List<ExpandFieldInfo> getPageListExpandFieldList(String expandCode);

    /**
     * 获取某一条拓展数据
     *
     * @author liaoxiting
     * @date 2022/4/1 9:55
     */
    Map<String, Object> getExpandDataInfo(String expandCode, Long primaryFieldValue);

}

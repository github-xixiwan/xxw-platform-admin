package com.xxw.platform.plugin.group.api;

import com.xxw.platform.plugin.group.api.pojo.SysGroupDTO;
import com.xxw.platform.plugin.group.api.pojo.SysGroupRequest;

import java.util.List;

/**
 * 业务分组Api
 *
 * @author liaoxiting
 * @date 2022-06-24 17:15:41
 */
public interface GroupApi {

    /**
     * 获取当前用户某个业务，某个分类下的业务数据id
     *
     * @author liaoxiting
     * @date 2022/5/11 17:00
     */
    List<Long> findUserGroupDataList(SysGroupRequest sysGroupRequest);

    /**
     * 获取当前用户某个业务下的分组列表
     *
     * @param sysGroupRequest 请求参数，一般传递groupBizCode即可
     * @param getTotal        是否获取用户全部的数据
     * @author liaoxiting
     * @date 2022/5/11 17:00
     */
    List<SysGroupDTO> findGroupList(SysGroupRequest sysGroupRequest, boolean getTotal);

    /**
     * 清空业务id的分组
     *
     * @author liaoxiting
     * @date 2022/7/22 23:40
     */
    void removeGroup(String groupBizCode, Long bizId);

}

package com.xxw.platform.plugin.group.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.group.api.GroupApi;
import com.xxw.platform.plugin.group.api.pojo.SysGroupDTO;
import com.xxw.platform.plugin.group.api.pojo.SysGroupRequest;
import com.xxw.platform.plugin.group.business.entity.SysGroup;

import java.util.List;

/**
 * 业务分组 服务类
 *
 * @author liaoxiting
 * @date 2022/05/11 12:54
 */
public interface SysGroupService extends IService<SysGroup>, GroupApi {

    /**
     * 添加时候的选择分组列表
     *
     * @author liaoxiting
     * @date 2022/5/11 17:00
     */
    List<SysGroupDTO> addSelect(SysGroupRequest sysGroupRequest);

    /**
     * 将某个业务记录，增加到某个分组中，如果分组没有则创建分组
     *
     * @param sysGroupRequest 请求参数
     * @author liaoxiting
     * @date 2022/05/11 12:54
     */
    void add(SysGroupRequest sysGroupRequest);

    /**
     * 删除
     *
     * @param sysGroupRequest 请求参数
     * @author liaoxiting
     * @date 2022/05/11 12:54
     */
    void del(SysGroupRequest sysGroupRequest);

}

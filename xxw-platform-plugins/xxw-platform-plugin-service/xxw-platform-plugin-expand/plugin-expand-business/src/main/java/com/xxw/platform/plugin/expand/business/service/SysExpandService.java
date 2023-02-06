package com.xxw.platform.plugin.expand.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.expand.api.ExpandApi;
import com.xxw.platform.plugin.expand.business.entity.SysExpand;
import com.xxw.platform.plugin.expand.business.entity.SysExpandData;
import com.xxw.platform.plugin.expand.business.request.SysExpandRequest;

import java.util.List;

/**
 * 业务拓展 服务类
 *
 * @author liaoxiting
 * @date 2022/03/29 23:47
 */
public interface SysExpandService extends IService<SysExpand>, ExpandApi {

    /**
     * 新增
     *
     * @param sysExpandRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void add(SysExpandRequest sysExpandRequest);

    /**
     * 删除
     *
     * @param sysExpandRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void del(SysExpandRequest sysExpandRequest);

    /**
     * 编辑
     *
     * @param sysExpandRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void edit(SysExpandRequest sysExpandRequest);

    /**
     * 查询详情
     *
     * @param sysExpandRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    SysExpand detail(SysExpandRequest sysExpandRequest);

    /**
     * 获取列表
     *
     * @param sysExpandRequest 请求参数
     * @return List<SysExpand>   返回结果
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    List<SysExpand> findList(SysExpandRequest sysExpandRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysExpandRequest 请求参数
     * @return PageResult<SysExpand>   返回结果
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    PageResult<SysExpand> findPage(SysExpandRequest sysExpandRequest);

    /**
     * 修改业务状态
     *
     * @author liaoxiting
     * @date 2022/3/30 10:37
     */
    void updateStatus(SysExpandRequest sysExpandRequest);

    /**
     * 获取业务元数据信息
     *
     * @author liaoxiting
     * @date 2022/3/31 15:26
     */
    SysExpandData getByExpandCode(SysExpandRequest sysExpandRequest);
}

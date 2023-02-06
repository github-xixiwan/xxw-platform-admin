package com.xxw.platform.plugin.expand.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.expand.business.entity.SysExpandData;
import com.xxw.platform.plugin.expand.business.request.SysExpandDataRequest;

import java.util.List;

/**
 * 业务拓展-具体数据 服务类
 *
 * @author liaoxiting
 * @date 2022/03/29 23:47
 */
public interface SysExpandDataService extends IService<SysExpandData> {

    /**
     * 新增
     *
     * @param sysExpandDataRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void add(SysExpandDataRequest sysExpandDataRequest);

    /**
     * 删除
     *
     * @param sysExpandDataRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void del(SysExpandDataRequest sysExpandDataRequest);

    /**
     * 编辑
     *
     * @param sysExpandDataRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void edit(SysExpandDataRequest sysExpandDataRequest);

    /**
     * 查询详情
     *
     * @param sysExpandDataRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    SysExpandData detail(SysExpandDataRequest sysExpandDataRequest);

    /**
     * 查询详情
     *
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    SysExpandData detailByPrimaryFieldValue(Long primaryFieldValue);

    /**
     * 获取列表
     *
     * @param sysExpandDataRequest 请求参数
     * @return List<SysExpandData>   返回结果
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    List<SysExpandData> findList(SysExpandDataRequest sysExpandDataRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysExpandDataRequest 请求参数
     * @return PageResult<SysExpandData>   返回结果
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    PageResult<SysExpandData> findPage(SysExpandDataRequest sysExpandDataRequest);

}

package com.xxw.platform.plugin.expand.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.expand.business.entity.SysExpandField;
import com.xxw.platform.plugin.expand.business.request.SysExpandFieldRequest;

import java.util.List;

/**
 * 业务拓展-字段信息 服务类
 *
 * @author liaoxiting
 * @date 2022/03/29 23:47
 */
public interface SysExpandFieldService extends IService<SysExpandField> {

	/**
     * 新增
     *
     * @param sysExpandFieldRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void add(SysExpandFieldRequest sysExpandFieldRequest);

	/**
     * 删除
     *
     * @param sysExpandFieldRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void del(SysExpandFieldRequest sysExpandFieldRequest);

	/**
     * 编辑
     *
     * @param sysExpandFieldRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    void edit(SysExpandFieldRequest sysExpandFieldRequest);

	/**
     * 查询详情
     *
     * @param sysExpandFieldRequest 请求参数
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    SysExpandField detail(SysExpandFieldRequest sysExpandFieldRequest);

	/**
     * 获取列表
     *
     * @param sysExpandFieldRequest        请求参数
     * @return List<SysExpandField>   返回结果
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    List<SysExpandField> findList(SysExpandFieldRequest sysExpandFieldRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysExpandFieldRequest              请求参数
     * @return PageResult<SysExpandField>   返回结果
     * @author liaoxiting
     * @date 2022/03/29 23:47
     */
    PageResult<SysExpandField> findPage(SysExpandFieldRequest sysExpandFieldRequest);

}

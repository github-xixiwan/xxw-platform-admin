package com.xxw.platform.plugin.config.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.config.api.InitConfigApi;
import com.xxw.platform.plugin.config.api.pojo.ConfigInitRequest;
import com.xxw.platform.plugin.config.business.entity.SysConfig;
import com.xxw.platform.plugin.config.business.pojo.InitConfigResponse;
import com.xxw.platform.plugin.config.business.pojo.param.SysConfigParam;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;

import java.util.List;

/**
 * 系统参数配置service接口
 *
 * @author liaoxiting
 * @date 2020/4/14 11:14
 */
public interface SysConfigService extends IService<SysConfig>, InitConfigApi {

    /**
     * 添加系统参数配置
     *
     * @param sysConfigParam 添加参数
     * @author liaoxiting
     * @date 2020/4/14 11:14
     */
    void add(SysConfigParam sysConfigParam);

    /**
     * 删除系统参数配置
     *
     * @param sysConfigParam 删除参数
     * @author liaoxiting
     * @date 2020/4/14 11:15
     */
    void del(SysConfigParam sysConfigParam);

    /**
     * 编辑系统参数配置
     *
     * @param sysConfigParam 编辑参数
     * @author liaoxiting
     * @date 2020/4/14 11:15
     */
    void edit(SysConfigParam sysConfigParam);

    /**
     * 查看系统参数配置
     *
     * @param sysConfigParam 查看参数
     * @return 系统参数配置
     * @author liaoxiting
     * @date 2020/4/14 11:15
     */
    SysConfig detail(SysConfigParam sysConfigParam);

    /**
     * 查询系统参数配置
     *
     * @param sysConfigParam 查询参数
     * @return 查询分页结果
     * @author liaoxiting
     * @date 2020/4/14 11:14
     */
    PageResult<SysConfig> findPage(SysConfigParam sysConfigParam);

    /**
     * 查询系统参数配置
     *
     * @param sysConfigParam 查询参数
     * @return 系统参数配置列表
     * @author liaoxiting
     * @date 2020/4/14 11:14
     */
    List<SysConfig> findList(SysConfigParam sysConfigParam);

    /**
     * 初始化配置参数
     *
     * @author liaoxiting
     * @date 2021/7/8 16:48
     */
    void initConfig(ConfigInitRequest configInitRequest);

    /**
     * 获取初始化的配置列表
     *
     * @author liaoxiting
     * @date 2021/7/8 17:49
     */
    InitConfigResponse getInitConfigs();

    /**
     * 获取后端部署的地址
     *
     * @author liaoxiting
     * @date 2022/3/3 14:23
     */
    String getServerDeployHost();
}

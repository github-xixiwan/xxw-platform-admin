package com.xxw.platform.plugin.config.business.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.config.api.pojo.ConfigInitRequest;
import com.xxw.platform.plugin.config.business.entity.SysConfig;
import com.xxw.platform.plugin.config.business.pojo.InitConfigResponse;
import com.xxw.platform.plugin.config.business.pojo.param.SysConfigParam;
import com.xxw.platform.plugin.config.business.service.SysConfigService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 参数配置控制器
 *
 * @author liaoxiting
 * @date 2020/4/13 22:46
 */
@RestController
@ApiResource(name = "参数配置控制器", resBizType = ResBizTypeEnum.SYSTEM)
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 添加系统参数配置
     *
     * @author liaoxiting
     * @date 2020/4/14 11:11
     */
    @PostResource(name = "添加系统参数配置", path = "/sysConfig/add")
    public ResponseData<?> add(@RequestBody @Validated(SysConfigParam.add.class) SysConfigParam sysConfigParam) {
        sysConfigService.add(sysConfigParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统参数配置
     *
     * @author liaoxiting
     * @date 2020/4/14 11:11
     */
    @PostResource(name = "删除系统参数配置", path = "/sysConfig/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysConfigParam.delete.class) SysConfigParam sysConfigParam) {
        sysConfigService.del(sysConfigParam);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑系统参数配置
     *
     * @author liaoxiting
     * @date 2020/4/14 11:11
     */
    @PostResource(name = "编辑系统参数配置", path = "/sysConfig/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysConfigParam.edit.class) SysConfigParam sysConfigParam) {
        sysConfigService.edit(sysConfigParam);
        return new SuccessResponseData<>();
    }

    /**
     * 查看系统参数配置
     *
     * @author liaoxiting
     * @date 2020/4/14 11:12
     */
    @GetResource(name = "查看系统参数配置", path = "/sysConfig/detail")
    public ResponseData<SysConfig> detail(@Validated(SysConfigParam.detail.class) SysConfigParam sysConfigParam) {
        return new SuccessResponseData<>(sysConfigService.detail(sysConfigParam));
    }
    /**
     * 分页查询配置列表
     *
     * @author liaoxiting
     * @date 2020/4/14 11:10
     */
    @GetResource(name = "分页查询配置列表", path = "/sysConfig/page")
    public ResponseData<PageResult<SysConfig>> page(SysConfigParam sysConfigParam) {
        return new SuccessResponseData<>(sysConfigService.findPage(sysConfigParam));
    }

    /**
     * 系统参数配置列表
     *
     * @author liaoxiting
     * @date 2020/4/14 11:10
     */
    @GetResource(name = "系统参数配置列表", path = "/sysConfig/list")
    public ResponseData<List<SysConfig>> list(SysConfigParam sysConfigParam) {
        return new SuccessResponseData<>(sysConfigService.findList(sysConfigParam));
    }

    /**
     * 获取系统配置是否初始化的标志
     *
     * @author liaoxiting
     * @date 2021/7/8 17:20
     */
    @GetResource(name = "获取系统配置是否初始化的标志", path = "/sysConfig/getInitConfigFlag", requiredPermission = false)
    public ResponseData<Boolean> getInitConfigFlag() {
        return new SuccessResponseData<>(sysConfigService.getInitConfigFlag());
    }

    /**
     * 初始化系统配置参数，用在系统第一次登录时
     *
     * @author liaoxiting
     * @date 2021/7/8 16:36
     */
    @PostResource(name = "初始化系统配置参数，用在系统第一次登录时", path = "/sysConfig/initConfig", requiredPermission = false)
    public ResponseData<?> initConfig(@RequestBody ConfigInitRequest configInitRequest) {
        sysConfigService.initConfig(configInitRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取需要初始化的配置列表
     *
     * @author liaoxiting
     * @date 2021/7/8 16:36
     */
    @GetResource(name = "获取需要初始化的配置列表", path = "/sysConfig/getInitConfigList")
    public ResponseData<InitConfigResponse> getInitConfigList() {
        return new SuccessResponseData<>(sysConfigService.getInitConfigs());
    }

    /**
     * 获取后端服务部署的地址
     *
     * @author liaoxiting
     * @date 2021/7/8 16:36
     */
    @GetResource(name = "获取后端服务部署的地址", path = "/sysConfig/getBackendDeployUrl", requiredLogin = false, requiredPermission = false)
    public ResponseData<String> getBackendDeployUrl() {
        String serverDeployHost = sysConfigService.getServerDeployHost();
        return new SuccessResponseData<>(serverDeployHost);
    }

}

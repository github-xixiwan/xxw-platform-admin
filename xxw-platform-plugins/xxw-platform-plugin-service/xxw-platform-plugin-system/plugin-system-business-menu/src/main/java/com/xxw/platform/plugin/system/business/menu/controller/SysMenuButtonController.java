package com.xxw.platform.plugin.system.business.menu.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.menu.SysMenuButtonRequest;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenuButton;
import com.xxw.platform.plugin.system.business.menu.service.SysMenuButtonService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统菜单按钮控制器
 *
 * @author liaoxiting
 * @date 2021/1/9 16:08
 */
@RestController
@ApiResource(name = "菜单按钮管理", resBizType = ResBizTypeEnum.SYSTEM)
public class SysMenuButtonController {

    @Resource
    private SysMenuButtonService sysMenuButtonService;

    /**
     * 添加系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮请求参数
     * @author liaoxiting
     * @date 2021/1/9 11:28
     */
    @PostResource(name = "添加系统菜单按钮", path = "/sysMenuButton/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(SysMenuButtonRequest.add.class) SysMenuButtonRequest sysMenuButtonRequest) {
        sysMenuButtonService.add(sysMenuButtonRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 添加系统菜单按钮-默认按钮
     *
     * @author liaoxiting
     * @date 2021/1/9 11:28
     */
    @PostResource(name = "添加系统默认菜单按钮", path = "/sysMenuButton/addSystemDefaultButton")
    @BusinessLog
    public ResponseData<?> addSystemDefaultButton(@RequestBody @Validated(SysMenuButtonRequest.def.class) SysMenuButtonRequest sysMenuButtonRequest) {
        sysMenuButtonService.addDefaultButtons(sysMenuButtonRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除单个系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮id
     * @author liaoxiting
     * @date 2021/1/9 12:14
     */
    @PostResource(name = "删除单个系统菜单按钮", path = "/sysMenuButton/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(SysMenuButtonRequest.delete.class) SysMenuButtonRequest sysMenuButtonRequest) {
        sysMenuButtonService.del(sysMenuButtonRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除多个系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮id集合
     * @author liaoxiting
     * @date 2021/1/9 12:27
     */
    @PostResource(name = "批量删除多个系统菜单按钮", path = "/sysMenuButton/batchDelete")
    @BusinessLog
    public ResponseData<?> batchDelete(@RequestBody @Validated(SysMenuButtonRequest.batchDelete.class) SysMenuButtonRequest sysMenuButtonRequest) {
        sysMenuButtonService.delBatch(sysMenuButtonRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑系统菜单按钮
     *
     * @param sysMenuButtonRequest 菜单按钮请求参数
     * @author liaoxiting
     * @date 2021/1/9 12:00
     */
    @PostResource(name = "编辑系统菜单按钮", path = "/sysMenuButton/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(SysMenuButtonRequest.edit.class) SysMenuButtonRequest sysMenuButtonRequest) {
        sysMenuButtonService.edit(sysMenuButtonRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取菜单按钮详情
     *
     * @param sysMenuButtonRequest 菜单按钮id
     * @author liaoxiting
     * @date 2021/1/9 11:53
     */
    @GetResource(name = "获取菜单按钮详情", path = "/sysMenuButton/detail")
    public ResponseData<SysMenuButton> detail(@Validated(SysMenuButtonRequest.detail.class) SysMenuButtonRequest sysMenuButtonRequest) {
        SysMenuButton detail = sysMenuButtonService.detail(sysMenuButtonRequest);
        return new SuccessResponseData<>(detail);
    }

    /**
     * 获取菜单按钮列表
     *
     * @param sysMenuButtonRequest 菜单id
     * @author liaoxiting
     * @date 2021/1/9 12:33
     */
    @GetResource(name = "获取菜单按钮列表", path = "/sysMenuButton/pageList")
    public ResponseData<PageResult<SysMenuButton>> pageList(@Validated(SysMenuButtonRequest.list.class) SysMenuButtonRequest sysMenuButtonRequest) {
        PageResult<SysMenuButton> pageResult = sysMenuButtonService.findPage(sysMenuButtonRequest);
        return new SuccessResponseData<>(pageResult);
    }

}

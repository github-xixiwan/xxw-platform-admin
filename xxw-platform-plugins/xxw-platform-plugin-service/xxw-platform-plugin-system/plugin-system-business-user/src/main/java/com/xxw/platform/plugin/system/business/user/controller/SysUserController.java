package com.xxw.platform.plugin.system.business.user.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.UserSelectTreeNode;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysUserRequest;
import com.xxw.platform.plugin.system.business.user.entity.SysUserRole;
import com.xxw.platform.plugin.system.business.user.service.SysUserRoleService;
import com.xxw.platform.plugin.system.business.user.service.SysUserService;
import com.xxw.platform.plugin.system.business.user.wrapper.UserExpandWrapper;
import com.xxw.platform.plugin.wrapper.api.annotation.Wrapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户管理控制器
 *
 * @author liaoxiting
 * @date 2020/11/6 09:47
 */
@RestController
@ApiResource(name = "用户管理", resBizType = ResBizTypeEnum.SYSTEM)
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 增加用户
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @PostResource(name = "系统用户_增加", path = "/sysUser/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(BaseRequest.add.class) SysUserRequest sysUserRequest) {
        sysUserService.add(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统用户
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @PostResource(name = "系统用户_删除", path = "/sysUser/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(SysUserRequest.delete.class) SysUserRequest sysUserRequest) {
        sysUserService.del(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除系统用户
     *
     * @author liaoxiting
     * @date 2021/4/7 16:12
     */
    @PostResource(name = "系统用户_批量删除系统用户", path = "/sysUser/batchDelete")
    @BusinessLog
    public ResponseData<?> batchDelete(@RequestBody @Validated(SysUserRequest.batchDelete.class) SysUserRequest sysUserRequest) {
        sysUserService.batchDelete(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑系统用户
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @PostResource(name = "系统用户_编辑", path = "/sysUser/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(SysUserRequest.edit.class) SysUserRequest sysUserRequest) {
        sysUserService.edit(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改状态
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @PostResource(name = "系统用户_修改状态", path = "/sysUser/changeStatus")
    @BusinessLog
    public ResponseData<?> changeStatus(@RequestBody @Validated(SysUserRequest.changeStatus.class) SysUserRequest sysUserRequest) {
        sysUserService.editStatus(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 重置密码
     *
     * @author liaoxiting
     * @date 2020/11/6 13:48
     */
    @PostResource(name = "系统用户_重置密码", path = "/sysUser/resetPwd")
    @BusinessLog
    public ResponseData<?> resetPwd(@RequestBody @Validated(SysUserRequest.resetPwd.class) SysUserRequest sysUserRequest) {
        sysUserService.resetPassword(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 授权角色
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @PostResource(name = "系统用户_授权角色", path = "/sysUser/grantRole")
    @BusinessLog
    public ResponseData<?> grantRole(@RequestBody @Validated(SysUserRequest.grantRole.class) SysUserRequest sysUserRequest) {
        sysUserService.grantRole(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 授权数据
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @PostResource(name = "系统用户_授权数据", path = "/sysUser/grantData")
    @BusinessLog
    public ResponseData<?> grantData(@RequestBody @Validated(SysUserRequest.grantData.class) SysUserRequest sysUserRequest) {
        sysUserService.grantData(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看系统用户
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @GetResource(name = "系统用户_查看", path = "/sysUser/detail")
    public ResponseData<SysUserDTO> detail(@Validated(SysUserRequest.detail.class) SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.detail(sysUserRequest));
    }

    /**
     * 获取当前登录用户的信息
     *
     * @author liaoxiting
     * @date 2021/1/1 19:01
     */
    @GetResource(name = "获取当前登录用户的信息", path = "/sysUser/currentUserInfo", requiredPermission = false)
    public ResponseData<SysUserDTO> currentUserInfo() {
        LoginUser loginUser = LoginContext.me().getLoginUser();

        SysUserRequest sysUserRequest = new SysUserRequest();
        sysUserRequest.setUserId(loginUser.getUserId());
        return new SuccessResponseData<>(sysUserService.detail(sysUserRequest));
    }

    /**
     * 查询系统用户
     *
     * @author liaoxiting
     * @date 2020/11/6 13:49
     */
    @GetResource(name = "系统用户_查询", path = "/sysUser/page")
    @Wrapper(UserExpandWrapper.class)
    public ResponseData<PageResult<SysUserDTO>> page(SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.findPage(sysUserRequest));
    }

    /**
     * 导出用户
     *
     * @author liaoxiting
     * @date 2020/11/6 13:57
     */
    @GetResource(name = "系统用户_导出", path = "/sysUser/export")
    @BusinessLog
    public void export(HttpServletResponse response) {
        sysUserService.export(response);
    }

    /**
     * 获取用户选择树数据（用在系统通知，选择发送人的时候）
     *
     * @author liaoxiting
     * @date 2021/1/15 8:28
     */
    @GetResource(name = "获取用户选择树数据（用在系统通知，选择发送人的时候）", path = "/sysUser/getUserSelectTree")
    public ResponseData<List<UserSelectTreeNode>> getUserTree() {
        return new SuccessResponseData<>(this.sysUserService.userSelectTree(new SysUserRequest()));
    }

    /**
     * 获取用户数据范围列表
     *
     * @author liaoxiting
     * @date 2020/11/6 13:51
     */
    @GetResource(name = "系统用户_获取用户数据范围列表", path = "/sysUser/getUserDataScope")
    public ResponseData<List<Long>> ownData(@Validated(SysUserRequest.detail.class) SysUserRequest sysUserRequest) {
        List<Long> userBindDataScope = sysUserService.getUserBindDataScope(sysUserRequest.getUserId());
        return new SuccessResponseData<>(userBindDataScope);
    }

    /**
     * 获取用户的角色列表
     *
     * @author liaoxiting
     * @date 2020/11/6 13:50
     */
    @GetResource(name = "系统用户_获取用户的角色列表", path = "/sysUser/getUserRoles")
    public ResponseData<List<SysUserRole>> ownRole(@Validated(SysUserRequest.detail.class) SysUserRequest sysUserRequest) {
        Long userId = sysUserRequest.getUserId();
        return new SuccessResponseData<>(sysUserRoleService.findListByUserId(userId));
    }

    /**
     * 用户下拉列表，可以根据姓名搜索
     * <p>
     * 本接口不查询超级管理员
     *
     * @param sysUserRequest 请求参数：name 姓名(可选)
     * @return 返回除超级管理员外的用户列表
     * @author liaoxiting
     * @date 2020/11/6 09:49
     */
    @GetResource(name = "系统用户_选择器", path = "/sysUser/selector")
    public ResponseData<List<SimpleDict>> selector(SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.selector(sysUserRequest));
    }

    /**
     * 用户下拉列表，可以根据姓名搜索
     * <p>
     * 本接口可查询到超级管理员，包含所有用户
     *
     * @param sysUserRequest 请求参数：name 姓名(可选)
     * @return 返回除超级管理员外的用户列表
     * @author liaoxiting
     * @date 2020/11/6 09:49
     */
    @GetResource(name = "系统用户_选择器", path = "/sysUser/selectorAll")
    public ResponseData<List<SimpleDict>> selectorAll(SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.selectorWithAdmin(sysUserRequest));
    }

    /**
     * 获取所有用户ID和名称列表
     *
     * @author liaoxiting
     * @date 2022/1/17 14:24
     **/
    @GetResource(name = "获取所有用户ID和名称列表", path = "/sysUser/getAllUserIdList")
    public ResponseData<List<SysUserRequest>> getAllUserIdList() {
        return new SuccessResponseData<>(sysUserService.getAllUserIdList());
    }

    /**
     * 运维平台接口检测
     *
     * @author liaoxiting
     * @date 2022/1/27 14:29
     **/
    @GetResource(name = "运维平台接口检测", path = "/sysUser/devopsApiCheck", requiredLogin = false, requiredPermission = false)
    public ResponseData<Integer> devopsApiCheck(SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.devopsApiCheck(sysUserRequest));
    }

    /**
     * 根据用户主键获取用户对应的token
     *
     * @author liaoxiting
     * @date 2022/1/17 14:24
     **/
    @GetResource(name = "根据用户主键获取用户对应的token", path = "/sysUser/getTokenByUserId")
    public ResponseData<String> getTokenByUserId(Long userId) {
        return new SuccessResponseData<>(sysUserService.getTokenByUserId(userId));
    }

    /**
     * 根据条件筛选用户
     *
     * @author liaoxiting
     * @date 2022/6/17 14:46
     */
    @GetResource(name = "根据条件筛选用户", path = "/sysUser/getUserListByConditions")
    public ResponseData<List<SimpleDict>> getUserListByConditions(SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.getUserListByConditions(sysUserRequest));
    }

    /**
     * 获取用户列表，通过用户id集合
     *
     * @author liaoxiting
     * @date 2022/9/25 10:27
     */
    @PostResource(name = "获取用户列表，通过用户id集合", path = "/sysUser/getUsersByUserIds")
    public ResponseData<List<SysUserDTO>> getUsersByUserIds(@RequestBody @Validated(SysUserRequest.getUserList.class) SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.getUserInfoList(sysUserRequest.getUserIds()));
    }

}

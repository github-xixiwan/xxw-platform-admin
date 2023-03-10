package com.xxw.platform.plugin.system.business.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.TreeNodeEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.frame.common.tree.factory.DefaultTreeBuildFactory;
import com.xxw.platform.plugin.auth.api.SessionManagerApi;
import com.xxw.platform.plugin.auth.api.constants.LoginCacheConstants;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.enums.DataScopeTypeEnum;
import com.xxw.platform.plugin.auth.api.exception.enums.AuthExceptionEnum;
import com.xxw.platform.plugin.auth.api.expander.AuthConfigExpander;
import com.xxw.platform.plugin.auth.api.password.PasswordStoredEncryptApi;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.auth.api.pojo.login.basic.SimpleUserInfo;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.expand.api.ExpandApi;
import com.xxw.platform.plugin.file.api.FileInfoApi;
import com.xxw.platform.plugin.file.api.constants.FileConstants;
import com.xxw.platform.plugin.jwt.api.context.JwtContext;
import com.xxw.platform.plugin.jwt.api.pojo.payload.DefaultJwtPayload;
import com.xxw.platform.plugin.message.api.expander.WebSocketConfigExpander;
import com.xxw.platform.plugin.office.api.OfficeExcelApi;
import com.xxw.platform.plugin.office.api.report.ExcelExportParam;
import com.xxw.platform.plugin.system.api.*;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import com.xxw.platform.plugin.system.api.enums.AntdvFrontTypeEnum;
import com.xxw.platform.plugin.system.api.enums.DevopsCheckStatusEnum;
import com.xxw.platform.plugin.system.api.enums.UserStatusEnum;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.user.SysUserExceptionEnum;
import com.xxw.platform.plugin.system.api.expander.SystemConfigExpander;
import com.xxw.platform.plugin.system.api.pojo.organization.DataScopeDTO;
import com.xxw.platform.plugin.system.api.pojo.organization.HrOrganizationDTO;
import com.xxw.platform.plugin.system.api.pojo.organization.HrPositionDTO;
import com.xxw.platform.plugin.system.api.pojo.role.dto.RoleAuthorizeInfo;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleDTO;
import com.xxw.platform.plugin.system.api.pojo.user.*;
import com.xxw.platform.plugin.system.api.pojo.user.request.OnlineUserRequest;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysUserRequest;
import com.xxw.platform.plugin.system.api.pojo.user.request.UserOrgRequest;
import com.xxw.platform.plugin.system.api.pojo.user.request.UserRoleRequest;
import com.xxw.platform.plugin.system.api.util.DataScopeUtil;
import com.xxw.platform.plugin.system.business.user.entity.SysUser;
import com.xxw.platform.plugin.system.business.user.entity.SysUserDataScope;
import com.xxw.platform.plugin.system.business.user.entity.SysUserOrg;
import com.xxw.platform.plugin.system.business.user.entity.SysUserRole;
import com.xxw.platform.plugin.system.business.user.factory.OnlineUserCreateFactory;
import com.xxw.platform.plugin.system.business.user.factory.SysUserCreateFactory;
import com.xxw.platform.plugin.system.business.user.factory.UserLoginInfoFactory;
import com.xxw.platform.plugin.system.business.user.mapper.SysUserMapper;
import com.xxw.platform.plugin.system.business.user.service.SysUserDataScopeService;
import com.xxw.platform.plugin.system.business.user.service.SysUserOrgService;
import com.xxw.platform.plugin.system.business.user.service.SysUserRoleService;
import com.xxw.platform.plugin.system.business.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ?????????????????????
 *
 * @author liaoxiting
 * @date 2020/11/21 15:04
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysUserDataScopeService sysUserDataScopeService;

    @Resource
    private OfficeExcelApi officeExcelApi;

    @Resource
    private DataScopeApi dataScopeApi;

    @Resource
    private RoleServiceApi roleServiceApi;

    @Resource
    private ResourceServiceApi resourceServiceApi;

    @Resource
    private FileInfoApi fileInfoApi;

    @Resource
    private PasswordStoredEncryptApi passwordStoredEncryptApi;

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private OrganizationServiceApi organizationServiceApi;

    @Resource
    private CacheOperatorApi<SysUserDTO> sysUserCacheOperatorApi;

    @Resource
    private PositionServiceApi positionServiceApi;

    @Resource
    private ExpandApi expandApi;

    @Resource(name = "loginErrorCountCacheApi")
    private CacheOperatorApi<Integer> loginErrorCountCacheApi;

    @Resource
    private MenuServiceApi menuServiceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserRequest sysUserRequest) {

        // ???????????????????????????????????????id
        Long organizationId = sysUserRequest.getOrgId();

        // ??????????????????????????????????????????
        DataScopeUtil.quickValidateDataScope(organizationId);

        // ??????bean???????????????????????????????????????
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUserRequest, sysUser, CopyOptions.create().ignoreError());
        SysUserCreateFactory.fillAddSysUser(sysUser);

        // ????????????????????????
        sysUser.setAvatar(FileConstants.DEFAULT_AVATAR_FILE_ID);

        // ????????????
        this.save(sysUser);

        // ????????????????????????
        if (null == sysUserRequest.getPositionId()) {
            sysUserOrgService.add(sysUser.getUserId(), sysUserRequest.getOrgId());
        } else {
            sysUserOrgService.add(sysUser.getUserId(), sysUserRequest.getOrgId(), sysUserRequest.getPositionId());
        }

        // ????????????????????????
        if (sysUserRequest.getExpandDataInfo() != null) {
            sysUserRequest.getExpandDataInfo().setPrimaryFieldValue(sysUser.getUserId());
            expandApi.saveOrUpdateExpandData(sysUserRequest.getExpandDataInfo());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // ???????????????????????????
        if (YesOrNotEnum.Y.getCode().equals(sysUser.getSuperAdminFlag())) {
            throw new SystemModularException(SysUserExceptionEnum.USER_CAN_NOT_DELETE_ADMIN);
        }

        // ????????????????????????????????????
        SysUserOrgDTO userOrgInfo = sysUserOrgService.getUserOrgByUserId(sysUser.getUserId());
        Long organizationId = userOrgInfo.getOrgId();

        // ??????????????????????????????????????????
        DataScopeUtil.quickValidateDataScope(organizationId);

        // ??????????????????????????????Y
        sysUser.setDelFlag(YesOrNotEnum.Y.getCode());
        this.updateById(sysUser);

        Long userId = sysUser.getUserId();

        // ???????????????????????????????????????
        sysUserOrgService.delByUserId(userId);

        // ??????????????????????????????-?????????????????????
        sysUserRoleService.delByUserId(userId);

        // ??????????????????????????????-???????????????????????????
        sysUserDataScopeService.delByUserId(userId);

        // ??????????????????????????????
        sysUserCacheOperatorApi.remove(String.valueOf(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysUserRequest sysUserRequest) {

        // ???????????????????????????????????????id
        Long organizationId = sysUserRequest.getOrgId();

        // ??????????????????????????????????????????
        DataScopeUtil.quickValidateDataScope(organizationId);

        // ???????????????
        SysUser sysUser = this.querySysUser(sysUserRequest);
        BeanUtil.copyProperties(sysUserRequest, sysUser, CopyOptions.create().ignoreError());

        // ??????????????????
        SysUserCreateFactory.fillEditSysUser(sysUser);
        this.updateById(sysUser);

        Long sysUserId = sysUser.getUserId();

        // ????????????????????????
        if (null == sysUserRequest.getPositionId()) {
            sysUserOrgService.edit(sysUser.getUserId(), sysUserRequest.getOrgId());
        } else {
            sysUserOrgService.edit(sysUser.getUserId(), sysUserRequest.getOrgId(), sysUserRequest.getPositionId());
        }

        // ??????????????????????????????
        sysUserCacheOperatorApi.remove(String.valueOf(sysUserId));

        // ????????????????????????
        if (sysUserRequest.getExpandDataInfo() != null) {
            sysUserRequest.getExpandDataInfo().setPrimaryFieldValue(sysUser.getUserId());
            expandApi.saveOrUpdateExpandData(sysUserRequest.getExpandDataInfo());
        }
    }

    @Override
    public void editInfo(SysUserRequest sysUserRequest) {

        // ???????????????????????????id
        sysUserRequest.setUserId(LoginContext.me().getLoginUser().getUserId());
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // ???????????????????????????
        SysUserCreateFactory.fillUpdateInfo(sysUserRequest, sysUser);

        this.updateById(sysUser);

        // ??????????????????????????????
        sysUserCacheOperatorApi.remove(String.valueOf(sysUser.getUserId()));
    }

    @Override
    public void editStatus(SysUserRequest sysUserRequest) {

        // ?????????????????????????????????
        Integer statusFlag = sysUserRequest.getStatusFlag();
        UserStatusEnum.validateUserStatus(statusFlag);

        SysUser sysUser = this.querySysUser(sysUserRequest);

        // ?????????????????????????????????
        if (YesOrNotEnum.Y.getCode().equals(sysUser.getSuperAdminFlag())) {
            throw new SystemModularException(SysUserExceptionEnum.USER_CAN_NOT_UPDATE_ADMIN);
        }

        Long id = sysUser.getUserId();

        // ???????????????????????????????????????????????????
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getUserId, id).and(i -> i.ne(SysUser::getDelFlag, YesOrNotEnum.Y.getCode())).set(SysUser::getStatusFlag, statusFlag);

        boolean update = this.update(updateWrapper);
        if (!update) {
            log.error(SysUserExceptionEnum.UPDATE_USER_STATUS_ERROR.getUserTip());
            throw new SystemModularException(SysUserExceptionEnum.UPDATE_USER_STATUS_ERROR);
        }

        // ??????????????????????????????
        sysUserCacheOperatorApi.remove(String.valueOf(sysUser.getUserId()));
    }

    @Override
    public void editPassword(SysUserRequest sysUserRequest) {

        // ?????????????????????userId
        LoginUser loginUser = LoginContext.me().getLoginUser();
        sysUserRequest.setUserId(loginUser.getUserId());

        SysUser sysUser = this.querySysUser(sysUserRequest);

        // ???????????????????????????
        if (sysUserRequest.getNewPassword().equals(sysUserRequest.getPassword())) {
            throw new SystemModularException(SysUserExceptionEnum.USER_PWD_REPEAT);
        }

        // ???????????????
        if (!passwordStoredEncryptApi.checkPassword(sysUserRequest.getPassword(), sysUser.getPassword())) {
            throw new SystemModularException(SysUserExceptionEnum.USER_PWD_ERROR);
        }

        sysUser.setPassword(passwordStoredEncryptApi.encrypt(sysUserRequest.getNewPassword()));
        this.updateById(sysUser);

        // ??????????????????????????????
        sysUserCacheOperatorApi.remove(String.valueOf(sysUser.getUserId()));
    }

    @Override
    public void resetPassword(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // ?????????????????????????????????
        String password = SystemConfigExpander.getDefaultPassWord();
        sysUser.setPassword(passwordStoredEncryptApi.encrypt(password));

        this.updateById(sysUser);
    }

    @Override
    public void editAvatar(SysUserRequest sysUserRequest) {

        // ???????????????id
        Long fileId = sysUserRequest.getAvatar();

        // ???????????????????????????id
        LoginUser loginUser = LoginContext.me().getLoginUser();
        sysUserRequest.setUserId(loginUser.getUserId());

        // ??????????????????
        SysUser sysUser = this.querySysUser(sysUserRequest);
        sysUser.setAvatar(fileId);
        this.updateById(sysUser);

        // ?????????????????????session??????
        SimpleUserInfo simpleUserInfo = loginUser.getSimpleUserInfo();
        simpleUserInfo.setAvatar(fileId);
        sessionManagerApi.updateSession(LoginContext.me().getToken(), loginUser);

        // ??????????????????????????????
        sysUserCacheOperatorApi.remove(String.valueOf(sysUser.getUserId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantRole(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // ?????????????????????????????????????????????
        SysUserOrgDTO userOrgInfo = sysUserOrgService.getUserOrgByUserId(sysUser.getUserId());
        Long organizationId = userOrgInfo.getOrgId();

        // ??????????????????????????????????????????
        DataScopeUtil.quickValidateDataScope(organizationId);

        // ?????????????????????
        sysUserRoleService.assignRoles(sysUserRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantData(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // ????????????????????????????????????
        SysUserOrgDTO userOrgInfo = sysUserOrgService.getUserOrgByUserId(sysUser.getUserId());
        Long organizationId = userOrgInfo.getOrgId();

        // ??????????????????????????????????????????
        DataScopeUtil.quickValidateDataScope(organizationId);

        sysUserDataScopeService.assignData(sysUserRequest);
    }

    @Override
    public SysUserDTO detail(SysUserRequest sysUserRequest) {
        SysUserDTO sysUserResponse = new SysUserDTO();

        // ????????????????????????
        SysUser sysUser = this.querySysUser(sysUserRequest);
        BeanUtil.copyProperties(sysUser, sysUserResponse, CopyOptions.create().ignoreError());

        // ??????????????????????????????
        SysUserOrgDTO userOrgInfo = sysUserOrgService.getUserOrgByUserId(sysUser.getUserId());
        if (ObjectUtil.isNotNull(userOrgInfo.getOrgId())) {
            sysUserResponse.setOrgId(userOrgInfo.getOrgId());
            HrOrganizationDTO orgDetail = organizationServiceApi.getOrgDetail(userOrgInfo.getOrgId());
            sysUserResponse.setOrgName(orgDetail.getOrgName());
        }
        if (ObjectUtil.isNotNull(userOrgInfo.getPositionId())) {
            sysUserResponse.setPositionId(userOrgInfo.getPositionId());
            HrPositionDTO positionDetail = positionServiceApi.getPositionDetail(userOrgInfo.getPositionId());
            sysUserResponse.setPositionName(positionDetail.getPositionName());
        }

        // ????????????????????????
        sysUserResponse.setGrantRoleIdList(sysUserRoleService.findRoleIdsByUserId(sysUser.getUserId()));

        return sysUserResponse;
    }

    @Override
    public PageResult<SysUserDTO> findPage(SysUserRequest sysUserRequest) {

        LoginUser loginUser = LoginContext.me().getLoginUser();

        // ???????????????????????????????????????
        Set<DataScopeTypeEnum> dataScopeTypeEnums = loginUser.getDataScopeTypeEnums();

        // ????????????????????????????????????????????????
        Set<Long> dataScopeOrganizationIds = loginUser.getDataScopeOrganizationIds();

        // ?????????????????????????????????????????????
        Set<Long> dataScopeUserIds = loginUser.getDataScopeUserIds();

        // ???????????????????????????
        if (dataScopeTypeEnums.contains(DataScopeTypeEnum.ALL)) {
            sysUserRequest.setScopeOrgIds(null);
            sysUserRequest.setUserScopeIds(null);
        }
        // ??????????????????????????????
        else if (dataScopeTypeEnums.contains(DataScopeTypeEnum.DEPT) || dataScopeTypeEnums.contains(DataScopeTypeEnum.DEPT_WITH_CHILD) || dataScopeTypeEnums.contains(DataScopeTypeEnum.DEFINE)) {
            sysUserRequest.setScopeOrgIds(dataScopeOrganizationIds);
            sysUserRequest.setUserScopeIds(null);
        }
        // ????????????????????????????????????
        else if (dataScopeTypeEnums.contains(DataScopeTypeEnum.SELF)) {
            sysUserRequest.setScopeOrgIds(null);
            sysUserRequest.setUserScopeIds(dataScopeUserIds);
        }
        // ??????????????????????????????????????????????????????
        else {
            sysUserRequest.setScopeOrgIds(null);
            sysUserRequest.setUserScopeIds(null);
        }

        Page<SysUserDTO> userPage = this.baseMapper.findUserPage(PageFactory.defaultPage(), sysUserRequest);

        // ????????????????????????????????????????????????????????????????????????????????????
        for (SysUserDTO record : userPage.getRecords()) {
            if (loginErrorCountCacheApi.contains(record.getAccount())) {
                Integer errorCount = loginErrorCountCacheApi.get(record.getAccount());
                if (errorCount >= LoginCacheConstants.MAX_ERROR_LOGIN_COUNT) {
                    record.setLoginErrorCountFlag(true);
                }
            }
        }

        return PageResultFactory.createPageResult(userPage);
    }

    @Override
    public List<SysUserDTO> getUserList(SysUserRequest sysUserRequest) {
        return this.baseMapper.findUserList(sysUserRequest);
    }

    @Override
    public void export(HttpServletResponse response) {
        ExcelExportParam excelExportParam = new ExcelExportParam();
        List<SysUser> sysUserList = this.list();

        excelExportParam.setClazz(SysUser.class);
        excelExportParam.setDataList(sysUserList);
        excelExportParam.setExcelTypeEnum(ExcelTypeEnum.XLS);
        excelExportParam.setFileName("??????????????????");
        excelExportParam.setResponse(response);

        officeExcelApi.easyExportDownload(excelExportParam);
    }

    @Override
    public List<UserSelectTreeNode> userSelectTree(SysUserRequest sysUserRequest) {
        // ??????????????????
        List<UserSelectTreeNode> treeNodeList = CollectionUtil.newArrayList();
        List<HrOrganizationDTO> orgList = organizationServiceApi.orgList();
        UserSelectTreeNode orgTreeNode;
        for (HrOrganizationDTO hrOrganization : orgList) {
            orgTreeNode = new UserSelectTreeNode();
            orgTreeNode.setId(String.valueOf(hrOrganization.getOrgId()));
            orgTreeNode.setPId(String.valueOf(hrOrganization.getOrgParentId()));
            orgTreeNode.setName(hrOrganization.getOrgName());
            orgTreeNode.setNodeType(TreeNodeEnum.ORG.getCode());
            orgTreeNode.setValue(String.valueOf(hrOrganization.getOrgId()));
            orgTreeNode.setSort(hrOrganization.getOrgSort());
            treeNodeList.add(orgTreeNode);
            List<UserSelectTreeNode> userNodeList = this.getUserTreeNodeList(hrOrganization.getOrgId(), treeNodeList);
            if (userNodeList.size() > 0) {
                treeNodeList.addAll(userNodeList);
            }
        }
        // ??????????????????
        return new DefaultTreeBuildFactory<UserSelectTreeNode>().doTreeBuild(treeNodeList);
    }

    @Override
    public SysUser getUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.ne(SysUser::getDelFlag, YesOrNotEnum.Y.getCode());

        List<SysUser> list = this.list(queryWrapper);

        // ???????????????
        if (list.isEmpty()) {
            throw new SystemModularException(SysUserExceptionEnum.USER_NOT_EXIST, account);
        }

        // ??????????????????
        if (list.size() > 1) {
            throw new SystemModularException(SysUserExceptionEnum.ACCOUNT_HAVE_MANY, account);
        }

        return list.get(0);
    }

    @Override
    public String getUserAvatarUrl(Long fileId) {

        // ???????????????????????????
        return fileInfoApi.getFileAuthUrl(fileId);
    }

    @Override
    public String getUserAvatarUrl(Long fileId, String token) {

        // ???????????????????????????
        return fileInfoApi.getFileAuthUrl(fileId, token);
    }

    @Override
    public List<UserSelectTreeNode> getUserTreeNodeList(Long orgId, List<UserSelectTreeNode> treeNodeList) {
        // ??????????????????
        List<UserSelectTreeNode> newTreeNodeList = CollectionUtil.newArrayList();
        SysUserRequest userRequest = new SysUserRequest();
        userRequest.setOrgId(orgId);
        List<SysUserDTO> userList = this.baseMapper.findUserList(userRequest);
        UserSelectTreeNode userTreeNode;
        for (SysUserDTO user : userList) {
            userTreeNode = new UserSelectTreeNode();
            userTreeNode.setId(String.valueOf(user.getUserId()));
            userTreeNode.setPId(String.valueOf(user.getOrgId()));
            userTreeNode.setName(user.getRealName());
            userTreeNode.setNodeType(TreeNodeEnum.USER.getCode());
            userTreeNode.setValue(String.valueOf(user.getUserId()));

            // ????????????treeNodeList????????????????????????????????????????????????????????????
            boolean fillThisUser = true;
            for (UserSelectTreeNode userSelectTreeNode : treeNodeList) {
                if (userSelectTreeNode.getNodeId().equals(userTreeNode.getId())) {
                    fillThisUser = false;
                    break;
                }
            }
            if (fillThisUser) {
                newTreeNodeList.add(userTreeNode);
            }
        }
        return newTreeNodeList;
    }

    @Override
    public List<SimpleDict> selector(SysUserRequest sysUserRequest) {
        return this.selectUserList(sysUserRequest, false);
    }

    @Override
    public List<SimpleDict> selectorWithAdmin(SysUserRequest sysUserRequest) {
        return this.selectUserList(sysUserRequest, true);
    }

    @Override
    public void batchDelete(SysUserRequest sysUserRequest) {
        List<Long> userIds = sysUserRequest.getUserIds();
        for (Long userId : userIds) {
            SysUserRequest tempRequest = new SysUserRequest();
            tempRequest.setUserId(userId);
            this.del(tempRequest);
        }
    }

    @Override
    public List<Long> getAllUserIds() {
        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.select(SysUser::getUserId);
        userLambdaQueryWrapper.eq(SysUser::getStatusFlag, StatusEnum.ENABLE.getCode());
        userLambdaQueryWrapper.ne(SysUser::getDelFlag, YesOrNotEnum.Y.getCode());

        List<SysUser> list = this.list(userLambdaQueryWrapper);
        return list.stream().map(SysUser::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<SysUserRequest> getAllUserIdList() {
        if (!SystemConfigExpander.getDevSwitchStatus()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.select(SysUser::getUserId, SysUser::getAccount);
        userLambdaQueryWrapper.eq(SysUser::getStatusFlag, StatusEnum.ENABLE.getCode());
        userLambdaQueryWrapper.ne(SysUser::getDelFlag, YesOrNotEnum.Y.getCode());
        List<SysUser> list = this.list(userLambdaQueryWrapper);
        return list.stream().map(item -> BeanUtil.toBean(item, SysUserRequest.class)).collect(Collectors.toList());
    }

    @Override
    public String getTokenByUserId(Long userId) {
        if (!SystemConfigExpander.getDevSwitchStatus() || !LoginContext.me().getSuperAdminFlag()) {
            return null;
        }
        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        SysUser sysUser = this.getOne(userLambdaQueryWrapper);

        // ????????????????????????????????????????????????
        UserLoginInfoDTO userValidateInfo = this.getUserLoginInfo(sysUser.getAccount());

        // ??????LoginUser????????????????????????
        LoginUser loginUser = userValidateInfo.getLoginUser();

        // ???????????????token
        DefaultJwtPayload defaultJwtPayload = new DefaultJwtPayload(loginUser.getUserId(), loginUser.getAccount(), false, null, null);
        String jwtToken = JwtContext.me().generateTokenDefaultPayload(defaultJwtPayload);
        loginUser.setToken(jwtToken);

        synchronized (this) {

            // ??????ws-url ????????????????????????
            loginUser.setWsUrl(WebSocketConfigExpander.getWebSocketWsUrl());

            // ?????????????????????????????????
            sessionManagerApi.createSession(jwtToken, loginUser, false);

            // ????????????????????????????????????????????????????????????????????????
            if (AuthConfigExpander.getSingleAccountLoginFlag()) {
                sessionManagerApi.removeSessionExcludeToken(jwtToken);
            }
        }

        return jwtToken;
    }

    @Override
    public Integer devopsApiCheck(SysUserRequest sysUserRequest) {
        String account = sysUserRequest.getAccount();
        String password = sysUserRequest.getPassword();
        SysUser sysUser = this.getUserByAccount(account);
        if (ObjectUtil.isEmpty(sysUser)) {
            return DevopsCheckStatusEnum.USER_NOT_EXIST.getCode();
        } else if (!passwordStoredEncryptApi.checkPassword(password, sysUser.getPassword())) {
            return DevopsCheckStatusEnum.ACCOUNT_PASSWORD_ERROR.getCode();
        } else if (!SystemConfigExpander.getDevSwitchStatus()) {
            return DevopsCheckStatusEnum.REQUESTER_NOT_OPEN_SWITCH.getCode();
        }
        return DevopsCheckStatusEnum.SUCCESSFUL.getCode();
    }

    @Override
    public List<SimpleDict> getUserListByConditions(SysUserRequest sysUserRequest) {

        Long orgId = sysUserRequest.getOrgId();
        Long roleId = sysUserRequest.getRoleId();
        Integer statusFlag = sysUserRequest.getStatusFlag();
        String condition = sysUserRequest.getCondition();

        // ????????????????????????????????????
        List<Long> orgUserIds = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(orgId)) {
            LambdaQueryWrapper<SysUserOrg> sysUserOrgLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserOrgLambdaQueryWrapper.eq(SysUserOrg::getOrgId, orgId);
            List<SysUserOrg> list = this.sysUserOrgService.list(sysUserOrgLambdaQueryWrapper);
            orgUserIds = list.stream().map(SysUserOrg::getUserId).collect(Collectors.toList());
            if (ObjectUtil.isEmpty(orgUserIds)) {
                return new ArrayList<>();
            }
        }

        // ???????????????????????????id
        List<Long> roleUserIds = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(roleId)) {
            LambdaQueryWrapper<SysUserRole> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleLambdaQueryWrapper.eq(SysUserRole::getRoleId, roleId);
            List<SysUserRole> list = this.sysUserRoleService.list(roleLambdaQueryWrapper);
            roleUserIds = list.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
            if (ObjectUtil.isEmpty(roleUserIds)) {
                return new ArrayList<>();
            }
        }

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // ??????????????????
        sysUserLambdaQueryWrapper.eq(SysUser::getDelFlag, YesOrNotEnum.N.getCode());

        // ??????????????????
        sysUserLambdaQueryWrapper.in(ObjectUtil.isNotEmpty(orgUserIds), SysUser::getUserId, orgUserIds);

        // ?????????????????????
        sysUserLambdaQueryWrapper.in(ObjectUtil.isNotEmpty(roleUserIds), SysUser::getUserId, roleUserIds);

        // ??????????????????
        sysUserLambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(statusFlag), SysUser::getStatusFlag, statusFlag);

        // ??????????????????
        sysUserLambdaQueryWrapper.nested(ObjectUtil.isNotEmpty(condition), i -> i.like(SysUser::getRealName, condition)
                .or(j -> j.like(SysUser::getAccount, condition)));

        List<SysUser> list = this.list(sysUserLambdaQueryWrapper);
        ArrayList<SimpleDict> results = new ArrayList<>();
        for (SysUser sysUser : list) {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setId(sysUser.getUserId());
            simpleDict.setName(sysUser.getRealName());
            results.add(simpleDict);
        }
        return results;
    }

    @Override
    public UserLoginInfoDTO getUserLoginInfo(String account) {

        // 1. ???????????????????????????
        SysUser sysUser = this.getUserByAccount(account);
        Long userId = sysUser.getUserId();

        // 2. ????????????????????????
        List<Long> roleIds = sysUserRoleService.findRoleIdsByUserId(userId);
        if (ObjectUtil.isEmpty(roleIds)) {
            throw new SystemModularException(AuthExceptionEnum.ROLE_IS_EMPTY);
        }
        List<SysRoleDTO> roleResponseList = roleServiceApi.getRolesByIds(roleIds);

        // 3. ???????????????????????????
        DataScopeDTO dataScopeResponse = dataScopeApi.getDataScope(userId, roleResponseList);

        // 4. ??????????????????????????????????????????
        SysUserOrgDTO userOrgInfo = sysUserOrgService.getUserOrgByUserId(userId);

        // ????????????????????????????????????????????????
        RoleAuthorizeInfo roleAuthorizeInfo = roleServiceApi.getRoleAuthorizeInfo(roleIds);

        // 4.1 ???????????????????????????????????????id
        List<Long> menuIdList = roleAuthorizeInfo.getMenuIdList();
        AntdvFrontTypeEnum userMenuType = menuServiceApi.getUserMenuType(menuIdList);

        // 5. ???????????????????????????url
        Set<String> resourceCodeList = roleAuthorizeInfo.getResourceCodeList();
        Set<String> resourceUrlsListByCodes = resourceServiceApi.getResourceUrlsListByCodes(resourceCodeList);

        // 6. ???????????????????????????code??????
        Set<String> roleButtonCodes = roleServiceApi.getRoleButtonCodes(roleIds);

        // 7. ??????????????????
        return UserLoginInfoFactory.userLoginInfoDTO(sysUser, roleResponseList, dataScopeResponse, userOrgInfo, resourceUrlsListByCodes, roleButtonCodes, userMenuType);
    }

    @Override
    public LoginUser getEffectiveLoginUser(LoginUser loginUser) {

        // ?????????C????????????????????????????????????????????????
        if (loginUser.getCustomerFlag()) {
            return loginUser;
        }

        UserLoginInfoDTO userLoginInfoDTO = this.getUserLoginInfo(loginUser.getAccount());
        LoginUser newLoginUser = userLoginInfoDTO.getLoginUser();

        // ???????????????????????????????????????
        newLoginUser.setToken(loginUser.getToken());
        newLoginUser.setTenantCode(loginUser.getTenantCode());
        newLoginUser.setWsUrl(loginUser.getWsUrl());
        newLoginUser.setOtherInfos(loginUser.getOtherInfos());

        return newLoginUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserLoginInfo(Long userId, Date date, String ip) {

        // ????????????id????????????????????????
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId).eq(SysUser::getDelFlag, YesOrNotEnum.N.getCode());
        SysUser sysUser = this.getOne(sysUserLambdaQueryWrapper);

        if (sysUser != null) {
            // ????????????????????????
            SysUser newSysUser = new SysUser();
            newSysUser.setUserId(sysUser.getUserId());
            newSysUser.setLastLoginIp(ip);
            newSysUser.setLastLoginTime(date);
            this.updateById(newSysUser);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserDataScopeListByOrgIdList(Set<Long> organizationIds) {
        if (organizationIds != null && organizationIds.size() > 0) {
            LambdaQueryWrapper<SysUserDataScope> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysUserDataScope::getOrgId, organizationIds);
            sysUserDataScopeService.remove(queryWrapper);
        }
    }

    @Override
    public List<Long> getUserRoleIdList(Long userId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        queryWrapper.select(SysUserRole::getRoleId);

        List<SysUserRole> list = sysUserRoleService.list(queryWrapper);
        return list.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserRoleListByRoleId(Long roleId) {
        if (roleId != null) {
            LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUserRole::getRoleId, roleId);
            sysUserRoleService.remove(queryWrapper);
        }
    }

    @Override
    public List<Long> getUserBindDataScope(Long userId) {
        return sysUserDataScopeService.findOrgIdsByUserId(userId);
    }

    @Override
    public List<OnlineUserDTO> onlineUserList(OnlineUserRequest onlineUserRequest) {
        List<LoginUser> loginUsers = sessionManagerApi.onlineUserList();

        // ????????????
        List<OnlineUserDTO> result = loginUsers.stream().map(OnlineUserCreateFactory::createOnlineUser).collect(Collectors.toList());

        // ???????????????????????????account????????????
        if (StrUtil.isNotBlank(onlineUserRequest.getAccount())) {
            return result.stream().filter(i -> i.getAccount().equals(onlineUserRequest.getAccount())).collect(Collectors.toList());
        } else {
            return result;
        }
    }

    @Override
    public SysUserDTO getUserInfoByUserId(Long userId) {

        // ?????????????????????
        SysUserDTO sysUserDTO = sysUserCacheOperatorApi.get(String.valueOf(userId));
        if (sysUserDTO != null) {
            return sysUserDTO;
        }

        SysUser sysUser = this.getById(userId);
        if (ObjectUtil.isNotEmpty(sysUser)) {
            SysUserDTO result = BeanUtil.copyProperties(sysUser, SysUserDTO.class);

            // ?????????????????????????????????
            LambdaQueryWrapper<SysUserOrg> sysUserOrgLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserOrgLambdaQueryWrapper.eq(SysUserOrg::getUserId, userId);
            SysUserOrg one = sysUserOrgService.getOne(sysUserOrgLambdaQueryWrapper, false);
            if (one != null) {
                Long orgId = one.getOrgId();
                HrOrganizationDTO orgDetail = this.organizationServiceApi.getOrgDetail(orgId);
                if (orgDetail != null) {
                    String orgName = orgDetail.getOrgName();
                    result.setOrgName(orgName);
                }
            }

            // ???????????????????????????
            String fileAuthUrl = fileInfoApi.getFileAuthUrl(sysUser.getAvatar());
            if (fileAuthUrl != null) {
                result.setAvatarUrl(fileAuthUrl);
            }

            sysUserCacheOperatorApi.put(String.valueOf(userId), result);
            return result;
        }
        return null;
    }

    @Override
    public List<SysUserDTO> getUserInfoList(List<Long> userIdSet) {

        if (ObjectUtil.isEmpty(userIdSet)) {
            return new ArrayList<>();
        }

        ArrayList<SysUserDTO> sysUserDTOS = new ArrayList<>();
        for (Long userId : userIdSet) {
            SysUserDTO sysUser = this.getUserInfoByUserId(userId);
            sysUserDTOS.add(sysUser);
        }

        return sysUserDTOS;
    }

    @Override
    public List<Long> queryAllUserIdList(SysUserRequest sysUserRequest) {

        LambdaQueryWrapper<SysUser> wrapper = createWrapper(sysUserRequest);

        // ?????????id
        wrapper.select(SysUser::getUserId);

        // ??????????????????ID
        Function<Object, Long> mapper = id -> Long.valueOf(id.toString());

        return this.listObjs(wrapper, mapper);
    }

    @Override
    public Boolean userExist(Long userId) {
        SysUserRequest userRequest = new SysUserRequest();
        userRequest.setUserId(userId);
        LambdaQueryWrapper<SysUser> wrapper = createWrapper(userRequest);

        // ?????????id
        wrapper.select(SysUser::getUserId);

        // ????????????
        SysUser sysUser = this.getOne(wrapper);
        if (sysUser == null || sysUser.getUserId() == null) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public String getUserAvatarUrlByUserId(Long userId) {

        // ????????????????????????id
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserId, userId).select(SysUser::getAvatar);
        SysUser sysUser = this.getOne(wrapper, false);

        if (sysUser == null) {
            return "";
        }

        return this.getUserAvatarUrl(sysUser.getAvatar());
    }

    @Override
    public SysUserDTO createAndSaveOAuth2User(OAuth2AuthUserDTO oAuth2AuthUserDTO) {

        // ??????bean???????????????????????????????????????
        SysUser oAuth2User = SysUserCreateFactory.createOAuth2User(oAuth2AuthUserDTO);
        SysUserCreateFactory.fillAddSysUser(oAuth2User);

        // ????????????????????????
        oAuth2User.setAvatar(FileConstants.DEFAULT_AVATAR_FILE_ID);

        // ????????????
        this.save(oAuth2User);

        // ??????OAuth2?????????????????????????????????????????????????????????
        UserRoleRequest userRoleRequest = new UserRoleRequest();
        userRoleRequest.setUserId(oAuth2User.getUserId());
        userRoleRequest.setRoleId(SystemConstants.OAUTH2_USER_ROLE_ID);
        this.sysUserRoleService.add(userRoleRequest);

        // ????????????????????????
        UserOrgRequest userOrgRequest = new UserOrgRequest();
        userOrgRequest.setUserId(oAuth2User.getUserId());
        userOrgRequest.setOrgId(SystemConstants.OAUTH2_USER_ORG_ID);
        this.sysUserOrgService.add(userOrgRequest);

        return BeanUtil.copyProperties(oAuth2User, SysUserDTO.class);
    }

    /**
     * ??????????????????
     *
     * @author liaoxiting
     * @date 2020/3/26 9:54
     */
    private SysUser querySysUser(SysUserRequest sysUserRequest) {

        SysUserDTO tempDTO = this.getUserInfoByUserId(sysUserRequest.getUserId());
        if (ObjectUtil.isNull(tempDTO)) {
            throw new SystemModularException(SysUserExceptionEnum.USER_NOT_EXIST, sysUserRequest.getUserId());
        }

        SysUser tempUser = new SysUser();
        BeanUtil.copyProperties(tempDTO, tempUser, CopyOptions.create().ignoreError());
        return tempUser;
    }

    /**
     * ?????????????????????wrapper
     *
     * @author liaoxiting
     * @date 2020/11/6 10:16
     */
    private LambdaQueryWrapper<SysUser> createWrapper(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        // ????????????????????????
        queryWrapper.eq(SysUser::getDelFlag, YesOrNotEnum.N.getCode());

        if (ObjectUtil.isEmpty(sysUserRequest)) {
            return queryWrapper;
        }

        // SQL??????
        queryWrapper.eq(ObjectUtil.isNotEmpty(sysUserRequest.getUserId()), SysUser::getUserId, sysUserRequest.getUserId());
        queryWrapper.like(ObjectUtil.isNotEmpty(sysUserRequest.getAccount()), SysUser::getAccount, sysUserRequest.getAccount());
        queryWrapper.like(ObjectUtil.isNotEmpty(sysUserRequest.getRealName()), SysUser::getRealName, sysUserRequest.getRealName());

        // ??????text??????
        if (ObjectUtil.isNotEmpty(sysUserRequest.getSearchText())) {
            queryWrapper.like(SysUser::getAccount, sysUserRequest.getSearchText()).or().like(SysUser::getRealName, sysUserRequest.getSearchText()).or()
                    .like(SysUser::getNickName, sysUserRequest.getSearchText());
        }

        return queryWrapper;
    }

    /**
     * ????????????????????????
     *
     * @param sysUserRequest ??????????????????
     * @param withAdminFlag  ????????????admin?????????true-??????
     * @author liaoxiting
     * @date 2022/9/19 20:55
     */
    private List<SimpleDict> selectUserList(SysUserRequest sysUserRequest, boolean withAdminFlag) {

        LambdaQueryWrapper<SysUser> wrapper = createWrapper(sysUserRequest);

        // ?????????????????????
        if (!withAdminFlag) {
            wrapper.ne(SysUser::getSuperAdminFlag, YesOrNotEnum.Y.getCode());
        }

        // ?????????id???name
        wrapper.select(SysUser::getRealName, SysUser::getUserId, SysUser::getAccount);
        List<SysUser> list = this.list(wrapper);

        ArrayList<SimpleDict> results = new ArrayList<>();
        for (SysUser sysUser : list) {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setId(sysUser.getUserId());
            simpleDict.setName(sysUser.getRealName());
            simpleDict.setCode(sysUser.getAccount());
            results.add(simpleDict);
        }

        return results;
    }

}

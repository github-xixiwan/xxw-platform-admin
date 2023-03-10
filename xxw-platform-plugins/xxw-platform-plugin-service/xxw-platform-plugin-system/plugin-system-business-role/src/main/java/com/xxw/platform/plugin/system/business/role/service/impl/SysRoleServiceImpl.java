package com.xxw.platform.plugin.system.business.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.constants.SymbolConstant;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.enums.DataScopeTypeEnum;
import com.xxw.platform.plugin.auth.api.exception.AuthException;
import com.xxw.platform.plugin.auth.api.exception.enums.AuthExceptionEnum;
import com.xxw.platform.plugin.auth.api.pojo.login.basic.SimpleRoleInfo;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.MenuServiceApi;
import com.xxw.platform.plugin.system.api.ResourceServiceApi;
import com.xxw.platform.plugin.system.api.UserServiceApi;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import com.xxw.platform.plugin.system.api.enums.AntdvFrontTypeEnum;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.role.SysRoleExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.menu.MenuAndButtonTreeResponse;
import com.xxw.platform.plugin.system.api.pojo.menu.SysMenuButtonDTO;
import com.xxw.platform.plugin.system.api.pojo.role.dto.*;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleMenuButtonRequest;
import com.xxw.platform.plugin.system.api.pojo.role.request.SysRoleRequest;
import com.xxw.platform.plugin.system.api.util.DataScopeUtil;
import com.xxw.platform.plugin.system.business.role.entity.*;
import com.xxw.platform.plugin.system.business.role.mapper.SysRoleMapper;
import com.xxw.platform.plugin.system.business.role.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * ????????????service???????????????
 *
 * @author liaoxiting
 * @date 2020/11/5 ??????11:33
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private UserServiceApi userServiceApi;

    @Resource
    private SysRoleResourceService sysRoleResourceService;

    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;

    @Resource
    private SysRoleMenuService roleMenuService;

    @Resource
    private SysRoleMenuButtonService sysRoleMenuButtonService;

    @Resource
    private MenuServiceApi menuServiceApi;

    @Resource
    private CacheOperatorApi<SysRole> roleInfoCacheApi;

    @Resource(name = "roleResourceCacheApi")
    private CacheOperatorApi<List<String>> roleResourceCacheApi;

    @Resource(name = "roleDataScopeCacheApi")
    private CacheOperatorApi<List<Long>> roleDataScopeCacheApi;

    @Resource
    private ResourceServiceApi resourceServiceApi;

    @Override
    public void add(SysRoleRequest sysRoleRequest) {

        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(sysRoleRequest, sysRole);

        // ?????????????????????
        sysRole.setAdminFlag(YesOrNotEnum.N.getCode());

        // ?????????????????????
        sysRole.setStatusFlag(StatusEnum.ENABLE.getCode());

        // ???????????????????????????
        sysRole.setRoleSystemFlag(YesOrNotEnum.N.getCode());

        //??????????????????
        sysRole.setDataScopeType(DataScopeTypeEnum.SELF.getCode());

        this.save(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // ???????????????????????????
        if (SystemConstants.SUPER_ADMIN_ROLE_CODE.equals(sysRole.getRoleCode())) {
            throw new ServiceException(SysRoleExceptionEnum.SYSTEM_ROLE_CANT_DELETE);
        }

        Long roleId = sysRole.getRoleId();

        // ????????????????????????????????????-????????????????????????
        sysRoleDataScopeService.delByRoleId(roleId);

        // ????????????????????????????????????-?????????????????????
        userServiceApi.deleteUserRoleListByRoleId(roleId);

        // ????????????????????????????????????-?????????????????????
        sysRoleResourceService.deleteRoleResourceListByRoleId(roleId, null);

        // ????????????????????????
        roleInfoCacheApi.remove(String.valueOf(roleId));

        // ?????????????????????????????????
        roleDataScopeCacheApi.remove(String.valueOf(sysRoleRequest.getRoleId()));

        // ????????????
        this.removeById(roleId);
    }

    @Override
    public void grantResourceV2GrantAll(SysRoleRequest sysRoleRequest) {

        // ?????????????????????????????????
        this.sysRoleResourceService.deleteRoleResourceListByRoleId(sysRoleRequest.getRoleId(), sysRoleRequest.getResourceBizType());

        // ????????????????????????????????????????????????????????????????????????????????????
        if (!sysRoleRequest.getTotalSelectFlag()) {
            return;
        }

        // ????????????????????????????????????????????????????????????????????????????????????
        ResBizTypeEnum resBizTypeEnum = null;
        if (ObjectUtil.isNotEmpty(sysRoleRequest.getResourceBizType())) {
            resBizTypeEnum = ResBizTypeEnum.DEFAULT.parseToEnum(sysRoleRequest.getResourceBizType().toString());
        }
        List<SysRoleResourceDTO> totalResourceCode = resourceServiceApi.getTotalResourceCode(resBizTypeEnum);
        this.sysRoleResourceService.batchSaveResCodes(sysRoleRequest.getRoleId(), totalResourceCode);
    }

    @Override
    public SysRoleDTO getRoleByCode(String roleCode) {
        if (roleCode == null) {
            return null;
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleCode, roleCode);
        sysRoleLambdaQueryWrapper.ne(SysRole::getDelFlag, YesOrNotEnum.Y.getCode());
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);

        if (sysRole == null) {
            return null;
        }

        SysRoleDTO roleResponse = new SysRoleDTO();
        BeanUtil.copyProperties(sysRole, roleResponse);

        return roleResponse;
    }

    @Override
    public RoleAuthorizeInfo getRoleAuthorizeInfo(List<Long> roleIdList) {

        HashSet<String> result = new HashSet<>();

        for (Long roleId : roleIdList) {

            // ??????????????????????????????????????????
            String key = String.valueOf(roleId);
            List<String> resourceCodesCache = roleResourceCacheApi.get(key);
            if (ObjectUtil.isNotEmpty(resourceCodesCache)) {
                result.addAll(resourceCodesCache);
                continue;
            }

            // ???????????????????????????????????????
            LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(SysRoleResource::getResourceCode);
            queryWrapper.eq(SysRoleResource::getRoleId, roleId);
            List<SysRoleResource> sysRoleResources = sysRoleResourceService.list(queryWrapper);
            List<String> sysResourceCodes = sysRoleResources.parallelStream().map(SysRoleResource::getResourceCode).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(sysResourceCodes)) {
                result.addAll(sysResourceCodes);
                roleResourceCacheApi.put(key, sysResourceCodes);
            }
        }

        // ???????????????????????????
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenu::getRoleId, roleIdList);
        wrapper.select(SysRoleMenu::getMenuId);
        List<SysRoleMenu> list = this.roleMenuService.list(wrapper);
        List<Long> menuIds = list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        // ?????????????????????????????????
        LambdaQueryWrapper<SysRoleMenuButton> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.in(SysRoleMenuButton::getRoleId, roleIdList);
        wrapper2.select(SysRoleMenuButton::getButtonId);
        List<SysRoleMenuButton> roleMenuButtons = this.sysRoleMenuButtonService.list(wrapper2);
        List<Long> buttonIds = roleMenuButtons.stream().map(SysRoleMenuButton::getButtonId).collect(Collectors.toList());

        // ??????????????????????????????????????????
        ArrayList<Long> businessIds = new ArrayList<>();
        businessIds.addAll(menuIds);
        businessIds.addAll(buttonIds);

        // ?????????????????????
        List<String> menuButtonResources = menuServiceApi.getResourceCodesByBusinessId(businessIds);
        result.addAll(menuButtonResources);

        // ??????????????????
        return new RoleAuthorizeInfo(menuIds, buttonIds, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // ?????????????????????????????????
        if (SystemConstants.SUPER_ADMIN_ROLE_CODE.equals(sysRole.getRoleCode())) {
            if (!sysRole.getRoleCode().equals(sysRoleRequest.getRoleCode())) {
                throw new SystemModularException(SysRoleExceptionEnum.SUPER_ADMIN_ROLE_CODE_ERROR);
            }
        }

        // ????????????
        BeanUtil.copyProperties(sysRoleRequest, sysRole);

        // ??????????????????????????????????????????????????????
        sysRole.setStatusFlag(null);

        this.updateById(sysRole);

        // ????????????????????????
        roleInfoCacheApi.remove(String.valueOf(sysRoleRequest.getRoleId()));
    }

    @Override
    public SysRoleDTO detail(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);
        SysRoleDTO roleResponse = new SysRoleDTO();
        BeanUtil.copyProperties(sysRole, roleResponse);

        // ??????????????????????????????
        roleResponse.setDataScopeTypeEnum(DataScopeTypeEnum.codeToEnum(sysRole.getDataScopeType()));

        return roleResponse;
    }

    @Override
    public PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = createWrapper(sysRoleRequest);

        // ???????????????????????????
        wrapper.eq(SysRole::getAdminFlag, YesOrNotEnum.N.getCode());

        Page<SysRole> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SimpleDict> findList(SysRoleRequest sysRoleParam) {
        List<SimpleDict> dictList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(sysRoleParam)) {

            // ???????????????????????????????????????
            if (ObjectUtil.isNotEmpty(sysRoleParam.getRoleName())) {
                queryWrapper.and(i -> i.like(SysRole::getRoleName, sysRoleParam.getRoleName()).or().like(SysRole::getRoleCode, sysRoleParam.getRoleName()));
            }
        }

        // ?????????????????????
        queryWrapper.eq(SysRole::getStatusFlag, StatusEnum.ENABLE.getCode());

        // ????????????????????????????????????????????????
        queryWrapper.orderByAsc(SysRole::getRoleSort);
        this.list(queryWrapper).forEach(sysRole -> {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setId(sysRole.getRoleId());
            simpleDict.setName(sysRole.getRoleName() + SymbolConstant.LEFT_SQUARE_BRACKETS + sysRole.getRoleCode() + SymbolConstant.RIGHT_SQUARE_BRACKETS);
            dictList.add(simpleDict);
        });
        return dictList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantMenuAndButton(SysRoleRequest sysRoleMenuButtonRequest) {

        // ??????????????????????????????
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLqw = new LambdaQueryWrapper<>();
        sysRoleMenuLqw.eq(SysRoleMenu::getRoleId, sysRoleMenuButtonRequest.getRoleId());
        roleMenuService.remove(sysRoleMenuLqw);

        // ????????????????????????????????????
        LambdaQueryWrapper<SysRoleMenuButton> menuButtonLqw = new LambdaQueryWrapper<>();
        menuButtonLqw.eq(SysRoleMenuButton::getRoleId, sysRoleMenuButtonRequest.getRoleId());
        sysRoleMenuButtonService.remove(menuButtonLqw);

        // ????????????
        List<Long> menuIdList = sysRoleMenuButtonRequest.getGrantMenuIdList();
        if (ObjectUtil.isNotEmpty(menuIdList)) {
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();

            // ??????ID
            Long roleId = sysRoleMenuButtonRequest.getRoleId();

            // ??????????????????????????????
            Set<Long> allParentMenuId = menuServiceApi.getMenuAllParentMenuId(new HashSet<>(menuIdList));

            // ?????????????????????
            for (Long menuId : allParentMenuId) {
                SysRoleMenu item = new SysRoleMenu();
                item.setRoleId(roleId);
                item.setMenuId(menuId);
                sysRoleMenus.add(item);
            }

            // ??????????????????
            for (Long menuId : menuIdList) {
                SysRoleMenu item = new SysRoleMenu();
                item.setRoleId(roleId);
                item.setMenuId(menuId);
                sysRoleMenus.add(item);
            }
            roleMenuService.saveBatch(sysRoleMenus);
        }

        // ????????????
        List<SysRoleMenuButtonRequest> menuButtonList = sysRoleMenuButtonRequest.getGrantMenuButtonIdList();
        if (ObjectUtil.isNotEmpty(menuButtonList)) {
            List<SysRoleMenuButton> sysRoleMenuButtons = new ArrayList<>();
            for (SysRoleMenuButtonRequest menuButton : menuButtonList) {
                SysRoleMenuButton item = new SysRoleMenuButton();
                item.setRoleId(sysRoleMenuButtonRequest.getRoleId());
                item.setButtonId(menuButton.getButtonId());
                item.setButtonCode(menuButton.getButtonCode());
                sysRoleMenuButtons.add(item);
            }
            sysRoleMenuButtonService.saveBatch(sysRoleMenuButtons);
        }
    }

    @Override
    public void grantMenu(SysRoleRequest sysRoleMenuButtonRequest) {

        // ??????????????????????????????
        Boolean grantAddMenuFlag = sysRoleMenuButtonRequest.getGrantAddMenuFlag();

        // ???????????????????????????
        if (grantAddMenuFlag) {
            SysRoleMenu item = new SysRoleMenu();
            item.setRoleId(sysRoleMenuButtonRequest.getRoleId());
            item.setMenuId(sysRoleMenuButtonRequest.getGrantMenuId());
            this.roleMenuService.save(item);
        } else {
            //???????????????????????????
            LambdaUpdateWrapper<SysRoleMenu> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SysRoleMenu::getRoleId, sysRoleMenuButtonRequest.getRoleId());
            wrapper.eq(SysRoleMenu::getMenuId, sysRoleMenuButtonRequest.getGrantMenuId());
            this.roleMenuService.remove(wrapper);
        }
    }

    @Override
    public List<MenuAndButtonTreeResponse> grantButton(SysRoleRequest sysRoleMenuButtonRequest) {
        // ?????????????????????????????????????????????
        List<Long> modularButtonIds = sysRoleMenuButtonRequest.getModularButtonIds();
        if (ObjectUtil.isNotEmpty(modularButtonIds)) {
            LambdaUpdateWrapper<SysRoleMenuButton> wrapper = new LambdaUpdateWrapper<>();
            wrapper.in(SysRoleMenuButton::getButtonId, modularButtonIds);
            wrapper.eq(SysRoleMenuButton::getRoleId, sysRoleMenuButtonRequest.getRoleId());
            this.sysRoleMenuButtonService.remove(wrapper);
        }

        // ??????????????????????????????????????????????????????
        List<Long> selectedButtonIds = sysRoleMenuButtonRequest.getSelectedButtonIds();
        if (ObjectUtil.isNotEmpty(selectedButtonIds)) {
            ArrayList<SysRoleMenuButton> sysRoleMenuButtons = new ArrayList<>();
            for (Long selectButtonId : selectedButtonIds) {
                SysRoleMenuButton sysRoleMenuButton = new SysRoleMenuButton();
                sysRoleMenuButton.setRoleId(sysRoleMenuButtonRequest.getRoleId());
                sysRoleMenuButton.setButtonId(selectButtonId);

                // ??????buttonId??????buttonCode
                String buttonCode = this.menuServiceApi.getMenuButtonCodeByButtonId(selectButtonId);
                sysRoleMenuButton.setButtonCode(buttonCode);

                sysRoleMenuButtons.add(sysRoleMenuButton);
            }
            this.sysRoleMenuButtonService.saveBatch(sysRoleMenuButtons, sysRoleMenuButtons.size());
        }

        // ?????????????????????????????????
        return this.menuServiceApi.getRoleBindOperateList(sysRoleMenuButtonRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantDataScope(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // ??????????????????????????????????????????
        boolean superAdmin = LoginContext.me().getSuperAdminFlag();

        // ???????????????????????????????????????
        Integer dataScopeType = sysRoleRequest.getDataScopeType();
        DataScopeTypeEnum dataScopeTypeEnum = DataScopeTypeEnum.codeToEnum(dataScopeType);

        // ?????????????????????????????????????????????????????????????????????
        if (!superAdmin) {

            // ?????????????????????????????????????????????
            if (DataScopeTypeEnum.ALL.equals(dataScopeTypeEnum)) {
                throw new AuthException(AuthExceptionEnum.ONLY_SUPER_ERROR);
            }

            // ?????????????????????????????????????????????????????????????????????????????????
            if (DataScopeTypeEnum.DEFINE.getCode().equals(dataScopeType)) {
                if (ObjectUtil.isEmpty(sysRoleRequest.getGrantOrgIdList())) {
                    throw new SystemModularException(SysRoleExceptionEnum.PLEASE_FILL_DATA_SCOPE);
                }
                for (Long orgId : sysRoleRequest.getGrantOrgIdList()) {
                    DataScopeUtil.quickValidateDataScope(orgId);
                }
            }
        }

        sysRole.setDataScopeType(sysRoleRequest.getDataScopeType());
        this.updateById(sysRole);

        // ??????????????????????????????
        sysRoleDataScopeService.grantDataScope(sysRoleRequest);

        // ?????????????????????????????????
        roleDataScopeCacheApi.remove(String.valueOf(sysRoleRequest.getRoleId()));
    }

    @Override
    public List<SimpleDict> dropDown() {
        List<SimpleDict> dictList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        // ??????????????????????????????????????????????????????????????????????????????
        if (!LoginContext.me().getSuperAdminFlag()) {

            // ?????????????????????
            List<SimpleRoleInfo> roles = LoginContext.me().getLoginUser().getSimpleRoleInfoList();

            // ??????????????????id
            Set<Long> loginUserRoleIds = roles.stream().map(SimpleRoleInfo::getRoleId).collect(Collectors.toSet());
            if (ObjectUtil.isEmpty(loginUserRoleIds)) {
                return dictList;
            }
            queryWrapper.in(SysRole::getRoleId, loginUserRoleIds);
        }

        // ?????????????????????
        queryWrapper.eq(SysRole::getStatusFlag, StatusEnum.ENABLE.getCode()).eq(SysRole::getDelFlag, YesOrNotEnum.N.getCode());

        // ??????????????????????????????
        queryWrapper.eq(SysRole::getAdminFlag, YesOrNotEnum.N.getCode());

        this.list(queryWrapper).forEach(sysRole -> {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setId(sysRole.getRoleId());
            simpleDict.setCode(sysRole.getRoleCode());
            simpleDict.setName(sysRole.getRoleName());
            dictList.add(simpleDict);
        });
        return dictList;
    }

    @Override
    public List<Long> getRoleDataScope(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);
        return sysRoleDataScopeService.getRoleDataScopeIdList(CollectionUtil.newArrayList(sysRole.getRoleId()));
    }

    @Override
    public void grantMenusAndButtons(SysRoleRequest sysRoleRequest) {

        // ???????????????????????????
        this.grantRoleMenus(sysRoleRequest);

        // ????????????
        if (ObjectUtil.isNotEmpty(sysRoleRequest.getModularButtonIds())) {
            this.grantButton(sysRoleRequest);
        }

    }

    @Override
    public List<MenuAndButtonTreeResponse> grantMenusAndButtonsAndGetResult(SysRoleRequest sysRoleRequest) {
        this.grantMenusAndButtons(sysRoleRequest);
        return menuServiceApi.getRoleMenuAndButtons(sysRoleRequest);
    }

    @Override
    public List<MenuAndButtonTreeResponse> grantRoleMenus(SysRoleRequest sysRoleRequest) {

        // ??????????????????????????????????????????
        Boolean selectBindFlag = sysRoleRequest.getSelectBindFlag();

        // ???????????????????????????
        Long roleId = sysRoleRequest.getRoleId();
        List<Long> grantMenuIdList = sysRoleRequest.getGrantMenuIdList();

        // ???????????????????????????
        if (selectBindFlag) {
            // ?????????????????????????????????????????????????????????????????????????????????????????????
            Set<Long> allParentMenuId = menuServiceApi.getMenuAllParentMenuId(new HashSet<>(grantMenuIdList));
            grantMenuIdList.addAll(allParentMenuId);

            // ?????????????????????????????????
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            for (Long menuId : grantMenuIdList) {
                SysRoleMenu item = new SysRoleMenu();
                item.setRoleId(roleId);
                item.setMenuId(menuId);
                sysRoleMenus.add(item);
            }
            this.roleMenuService.saveBatch(sysRoleMenus);
        } else {
            // ???????????????????????????
            LambdaUpdateWrapper<SysRoleMenu> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SysRoleMenu::getRoleId, roleId);
            wrapper.in(SysRoleMenu::getMenuId, grantMenuIdList);
            this.roleMenuService.remove(wrapper);
        }

        // ???????????????????????????????????????
        return menuServiceApi.getRoleBindMenuList(sysRoleRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public List<MenuAndButtonTreeResponse> grantRoleMenusGrantAll(SysRoleRequest sysRoleRequest) {

        // ???????????????????????????????????????????????????id
        AntdvFrontTypeEnum antdvFrontTypeEnum = AntdvFrontTypeEnum.parseToEnum(sysRoleRequest.getResourceBizType());
        List<Long> totalMenuIdList = this.menuServiceApi.getTotalMenuIdList(antdvFrontTypeEnum);

        // ?????????????????????????????????
        LambdaUpdateWrapper<SysRoleMenu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, sysRoleRequest.getRoleId());
        wrapper.in(SysRoleMenu::getMenuId, totalMenuIdList);
        this.roleMenuService.remove(wrapper);

        // ???????????????????????????????????????
        List<MenuAndButtonTreeResponse> roleBindMenuList = menuServiceApi.getRoleBindMenuList(sysRoleRequest);

        // ???????????????????????????????????????
        if (!sysRoleRequest.getTotalSelectFlag()) {
            return roleBindMenuList;
        }
        // ?????????????????????????????????
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        for (Long menuId : totalMenuIdList) {
            SysRoleMenu item = new SysRoleMenu();
            item.setRoleId(sysRoleRequest.getRoleId());
            item.setMenuId(menuId);
            sysRoleMenus.add(item);
        }
        this.roleMenuService.saveBatch(sysRoleMenus);

        return roleBindMenuList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public List<MenuAndButtonTreeResponse> grantButtonGrantAll(SysRoleRequest sysRoleRequest) {

        // ???????????????????????????????????????
        LambdaUpdateWrapper<SysRoleMenuButton> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysRoleMenuButton::getRoleId, sysRoleRequest.getRoleId());
        this.sysRoleMenuButtonService.remove(wrapper);

        // ???????????????????????????????????????
        List<MenuAndButtonTreeResponse> roleBindOperateList = menuServiceApi.getRoleBindOperateList(sysRoleRequest);

        // ???????????????????????????????????????
        if (!sysRoleRequest.getTotalSelectFlag()) {
            return roleBindOperateList;
        }

        // ??????????????????????????????
        AntdvFrontTypeEnum antdvFrontTypeEnum = AntdvFrontTypeEnum.parseToEnum(sysRoleRequest.getResourceBizType());
        List<SysMenuButtonDTO> totalButtonIds = this.menuServiceApi.getTotalMenuButtonIdList(antdvFrontTypeEnum);

        // ?????????????????????????????????
        List<SysRoleMenuButton> sysRoleMenuButtons = new ArrayList<>();
        for (SysMenuButtonDTO buttonDTO : totalButtonIds) {
            SysRoleMenuButton item = new SysRoleMenuButton();
            item.setRoleId(sysRoleRequest.getRoleId());
            item.setButtonId(buttonDTO.getButtonId());
            item.setButtonCode(buttonDTO.getButtonCode());
            sysRoleMenuButtons.add(item);
        }
        this.sysRoleMenuButtonService.batchSaveRoleMenuButton(sysRoleMenuButtons);

        return roleBindOperateList;
    }

    @Override
    public List<SysRoleDTO> getRolesByIds(List<Long> roleIds) {
        ArrayList<SysRoleDTO> sysRoleResponses = new ArrayList<>();
        for (Long roleId : roleIds) {
            SysRoleRequest sysRoleRequest = new SysRoleRequest();
            sysRoleRequest.setRoleId(roleId);
            SysRoleDTO detail = this.detail(sysRoleRequest);
            sysRoleResponses.add(detail);
        }
        return sysRoleResponses;
    }

    @Override
    public List<Long> getRoleDataScopes(List<Long> roleIds) {

        ArrayList<Long> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(roleIds)) {
            return result;
        }

        for (Long roleId : roleIds) {
            // ???????????????????????????
            String key = String.valueOf(roleId);
            List<Long> scopes = roleDataScopeCacheApi.get(key);
            if (scopes != null) {
                result.addAll(scopes);
            }

            // ??????????????????????????????
            LambdaQueryWrapper<SysRoleDataScope> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysRoleDataScope::getRoleId, roleId);
            List<SysRoleDataScope> list = this.sysRoleDataScopeService.list(queryWrapper);
            if (!list.isEmpty()) {
                List<Long> realScopes = list.stream().map(SysRoleDataScope::getOrganizationId).collect(Collectors.toList());
                result.addAll(realScopes);

                // ????????????????????????
                roleDataScopeCacheApi.put(key, realScopes);
            }
        }

        return result;
    }

    @Override
    public List<Long> getMenuIdsByRoleIds(List<Long> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        // ???????????????????????????
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenu::getRoleId, roleIds);
        queryWrapper.select(SysRoleMenu::getMenuId);

        List<SysRoleMenu> roleMenus = this.roleMenuService.list(queryWrapper);
        if (roleMenus == null || roleMenus.isEmpty()) {
            return new ArrayList<>();
        }

        return roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    public Set<String> getRoleResourceCodeList(List<Long> roleIdList) {
        RoleAuthorizeInfo roleAuthorizeInfo = this.getRoleAuthorizeInfo(roleIdList);
        return roleAuthorizeInfo.getResourceCodeList();
    }

    @Override
    public List<SysRoleResourceDTO> getRoleResourceList(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleResource::getRoleId, roleIdList);
        List<SysRoleResource> sysRoleResources = sysRoleResourceService.list(queryWrapper);
        return sysRoleResources.parallelStream().map(item -> BeanUtil.copyProperties(item, SysRoleResourceDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Set<String> getRoleButtonCodes(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleMenuButton> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenuButton::getRoleId, roleIdList);
        queryWrapper.select(SysRoleMenuButton::getButtonCode);
        List<SysRoleMenuButton> list = sysRoleMenuButtonService.list(queryWrapper);
        return list.stream().map(SysRoleMenuButton::getButtonCode).collect(Collectors.toSet());
    }

    @Override
    public List<SysRoleMenuDTO> getRoleMenuList(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getRoleId, roleIdList);
        List<SysRoleMenu> roleMenus = roleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        return roleMenus.parallelStream().map(item -> BeanUtil.copyProperties(item, SysRoleMenuDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SysRoleMenuButtonDTO> getRoleMenuButtonList(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleMenuButton> sysRoleMenuButtonLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuButtonLambdaQueryWrapper.in(SysRoleMenuButton::getRoleId, roleIdList);
        List<SysRoleMenuButton> sysRoleMenuButtons = sysRoleMenuButtonService.list(sysRoleMenuButtonLambdaQueryWrapper);
        return sysRoleMenuButtons.parallelStream().map(item -> BeanUtil.copyProperties(item, SysRoleMenuButtonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SysRoleDTO> getRoleSelectList(SysRoleRequest sysRoleRequest) {

        LambdaQueryWrapper<SysRole> wrapper = this.createWrapper(sysRoleRequest);
        List<SysRole> list = this.list(wrapper);

        ArrayList<SysRoleDTO> sysRoleDTOS = new ArrayList<>();
        for (SysRole sysRole : list) {
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            BeanUtil.copyProperties(sysRole, sysRoleDTO);
            sysRoleDTOS.add(sysRoleDTO);
        }

        return sysRoleDTOS;
    }

    @Override
    public void addAdminRole(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = new SysRole();

        sysRole.setRoleId(sysRoleRequest.getRoleId());
        sysRole.setRoleName(sysRoleRequest.getRoleName());
        sysRole.setRoleCode(sysRoleRequest.getRoleCode());

        sysRole.setRoleSort(new BigDecimal(9999));
        sysRole.setDataScopeType(DataScopeTypeEnum.ALL.getCode());
        sysRole.setStatusFlag(StatusEnum.ENABLE.getCode());
        sysRole.setAdminFlag(YesOrNotEnum.Y.getCode());
        sysRole.setRoleSystemFlag(YesOrNotEnum.Y.getCode());
        sysRole.setDelFlag(YesOrNotEnum.N.getCode());

        this.save(sysRole);
    }

    /**
     * ??????????????????
     *
     * @param sysRoleRequest ????????????
     * @author liaoxiting
     * @date 2020/11/5 ??????4:12
     */
    private SysRole querySysRole(SysRoleRequest sysRoleRequest) {

        // ??????????????????????????????
        String key = String.valueOf(sysRoleRequest.getRoleId());
        SysRole sysRoleCache = roleInfoCacheApi.get(key);
        if (sysRoleCache != null) {
            return sysRoleCache;
        }

        SysRole sysRole = this.getById(sysRoleRequest.getRoleId());
        if (ObjectUtil.isNull(sysRole)) {
            throw new SystemModularException(SysRoleExceptionEnum.ROLE_NOT_EXIST);
        }

        // ????????????
        roleInfoCacheApi.put(key, sysRole);

        return sysRole;
    }

    /**
     * ????????????wrapper
     *
     * @author liaoxiting
     * @date 2020/11/22 15:14
     */
    private LambdaQueryWrapper<SysRole> createWrapper(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        // ??????????????????
        queryWrapper.eq(SysRole::getDelFlag, YesOrNotEnum.N.getCode());

        // ????????????????????????????????????????????????
        queryWrapper.orderByAsc(SysRole::getRoleSort);

        if (ObjectUtil.isEmpty(sysRoleRequest)) {
            return queryWrapper;
        }

        // ????????????????????????
        queryWrapper.like(ObjectUtil.isNotEmpty(sysRoleRequest.getRoleName()), SysRole::getRoleName, sysRoleRequest.getRoleName());

        // ????????????????????????
        queryWrapper.like(ObjectUtil.isNotEmpty(sysRoleRequest.getRoleCode()), SysRole::getRoleCode, sysRoleRequest.getRoleCode());

        // ????????????????????????????????????
        queryWrapper.eq(ObjectUtil.isNotEmpty(sysRoleRequest.getAdminFlag()), SysRole::getAdminFlag, sysRoleRequest.getAdminFlag());

        return queryWrapper;
    }

}

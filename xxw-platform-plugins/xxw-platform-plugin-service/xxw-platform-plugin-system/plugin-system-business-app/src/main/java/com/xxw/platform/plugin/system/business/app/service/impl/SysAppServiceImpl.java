package com.xxw.platform.plugin.system.business.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.AppServiceApi;
import com.xxw.platform.plugin.system.api.MenuServiceApi;
import com.xxw.platform.plugin.system.api.exception.enums.app.AppExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.app.SysAppRequest;
import com.xxw.platform.plugin.system.api.pojo.app.SysAppResult;
import com.xxw.platform.plugin.system.business.app.entity.SysApp;
import com.xxw.platform.plugin.system.business.app.mapper.SysAppMapper;
import com.xxw.platform.plugin.system.business.app.service.SysAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统应用service接口实现类
 *
 * @author liaoxiting
 * @date 2020/3/13 16:15
 */
@Service
public class SysAppServiceImpl extends ServiceImpl<SysAppMapper, SysApp> implements SysAppService, AppServiceApi {

    @Resource
    private MenuServiceApi menuApi;

    @Override
    public void add(SysAppRequest sysAppRequest) {

        SysApp sysApp = new SysApp();

        // 设置名称和编码
        sysApp.setAppName(sysAppRequest.getAppName());
        sysApp.setAppCode(sysAppRequest.getAppCode());
        sysApp.setAppIcon(sysAppRequest.getAppIcon());

        // 默认排序值
        if (sysAppRequest.getAppSort() == null) {
            sysApp.setAppSort(999);
        } else {
            sysApp.setAppSort(sysAppRequest.getAppSort());
        }

        // 默认不激活
        sysApp.setActiveFlag(YesOrNotEnum.N.getCode());

        // 设为启用
        sysApp.setStatusFlag(StatusEnum.ENABLE.getCode());

        this.save(sysApp);
    }

    @Override
    public void del(SysAppRequest sysAppRequest) {
        SysApp sysApp = this.querySysApp(sysAppRequest);
        String code = sysApp.getAppCode();

        // 该应用下有菜单，则不能删除
        boolean hasMenu = menuApi.hasMenu(code);
        if (hasMenu) {
            throw new ServiceException(AppExceptionEnum.APP_CANNOT_DELETE);
        }

        // 逻辑删除
        sysApp.setDelFlag(YesOrNotEnum.Y.getCode());

        this.updateById(sysApp);
    }

    @Override
    public void edit(SysAppRequest sysAppRequest) {

        SysApp sysApp = this.querySysApp(sysAppRequest);
        BeanUtil.copyProperties(sysAppRequest, sysApp);

        // 不能修改编码
        sysApp.setAppCode(null);

        // 不能修改状态，修改状态接口修改状态
        sysApp.setStatusFlag(null);

        // 不能修改激活，激活接口激活应用
        sysApp.setActiveFlag(null);

        this.updateById(sysApp);
    }

    @Override
    public void editStatus(SysAppRequest sysAppParam) {
        SysApp currentApp = this.querySysApp(sysAppParam);
        currentApp.setStatusFlag(sysAppParam.getStatusFlag());
        this.updateById(currentApp);
    }

    @Override
    public SysApp detail(SysAppRequest sysAppRequest) {
        return this.querySysApp(sysAppRequest);
    }

    @Override
    public List<SysApp> findList(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> wrapper = createWrapper(sysAppRequest);
        return this.list(wrapper);
    }

    @Override
    public PageResult<SysApp> findPage(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> wrapper = createWrapper(sysAppRequest);
        Page<SysApp> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActiveFlag(SysAppRequest sysAppRequest) {
        SysApp currentApp = this.querySysApp(sysAppRequest);

        // 如果应用下没有菜单，不能激活
        boolean hasMenu = menuApi.hasMenu(currentApp.getAppCode());
        if (!hasMenu) {
            throw new ServiceException(AppExceptionEnum.ACTIVE_ERROR);
        }

        // 所有已激活的改为未激活
        LambdaUpdateWrapper<SysApp> sysAppLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysAppLambdaUpdateWrapper.set(SysApp::getActiveFlag, YesOrNotEnum.N.getCode());
        sysAppLambdaUpdateWrapper.eq(SysApp::getActiveFlag, YesOrNotEnum.Y.getCode());
        this.update(sysAppLambdaUpdateWrapper);

        // 当前的设置为已激活
        currentApp.setActiveFlag(YesOrNotEnum.Y.getCode());
        this.updateById(currentApp);
    }

    @Override
    public List<SysApp> getUserTopAppList() {

        // 获取用户拥有的appCode列表
        List<String> userAppCodeList = menuApi.getUserAppCodeList();

        // 根据appCode获取对应的app详情
        LambdaQueryWrapper<SysApp> wrapper = this.createWrapper(null);
        wrapper.in(SysApp::getAppCode, userAppCodeList);

        // 仅查询code和name
        wrapper.select(SysApp::getAppName, SysApp::getAppCode);

        // 根据激活顺序排序
        wrapper.orderByDesc(SysApp::getActiveFlag);

        return this.list(wrapper);
    }

    @Override
    public Set<SimpleDict> getAppsByAppCodes(Set<String> appCodes) {
        HashSet<SimpleDict> simpleDicts = new HashSet<>();

        LambdaQueryWrapper<SysApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysApp::getAppCode, appCodes);
        queryWrapper.select(SysApp::getAppId, SysApp::getAppCode, SysApp::getAppName);

        List<SysApp> list = this.list(queryWrapper);
        for (SysApp sysApp : list) {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setId(sysApp.getAppId());
            simpleDict.setCode(sysApp.getAppCode());
            simpleDict.setName(sysApp.getAppName());
            simpleDicts.add(simpleDict);
        }

        return simpleDicts;
    }

    @Override
    public String getAppNameByAppCode(String appCode) {

        String emptyName = "空应用";

        if (ObjectUtil.isEmpty(appCode)) {
            return emptyName;
        }

        LambdaQueryWrapper<SysApp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysApp::getAppCode, appCode);
        lambdaQueryWrapper.select(SysApp::getAppName);
        SysApp sysApp = this.getOne(lambdaQueryWrapper);

        if (sysApp == null || ObjectUtil.isEmpty(sysApp.getAppName())) {
            return emptyName;
        } else {
            return sysApp.getAppName();
        }
    }

    @Override
    public String getActiveAppCode() {
        LambdaQueryWrapper<SysApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysApp::getAppCode);
        queryWrapper.eq(SysApp::getActiveFlag, YesOrNotEnum.Y.getCode());
        queryWrapper.eq(SysApp::getDelFlag, YesOrNotEnum.N.getCode());
        List<SysApp> list = this.list(queryWrapper);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0).getAppCode();
        }
    }

    @Override
    public SysAppResult getAppInfoByAppCode(String appCode) {
        LambdaQueryWrapper<SysApp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysApp::getAppCode, appCode);
        SysApp sysApp = this.getOne(lambdaQueryWrapper, false);

        if (sysApp != null) {
            SysAppResult sysAppResult = new SysAppResult();
            BeanUtil.copyProperties(sysApp, sysAppResult);
            return sysAppResult;
        } else {
            return new SysAppResult();
        }
    }

    @Override
    public List<SysAppResult> getSortedApps() {
        return this.getSortedApps(false);
    }

    @Override
    public List<SysAppResult> getSortedApps(Boolean devopsFlag) {
        LambdaQueryWrapper<SysApp> wrapper = this.createWrapper(new SysAppRequest());

        // 只查询应用名称和应用编码
        wrapper.select(SysApp::getAppName, SysApp::getAppCode);

        // 只查询启用的应用
        wrapper.eq(SysApp::getStatusFlag, StatusEnum.ENABLE.getCode());

        // 是否查询运维平台的菜单
        if (devopsFlag == null || !devopsFlag) {
            wrapper.ne(SysApp::getDevopsFlag, YesOrNotEnum.Y.getCode());
        }

        List<SysApp> list = this.list(wrapper);

        return list.stream().map(i -> {
            SysAppResult target = new SysAppResult();
            BeanUtil.copyProperties(i, target);
            return target;
        }).collect(Collectors.toList());
    }

    /**
     * 获取系统应用
     *
     * @author liaoxiting
     * @date 2020/3/26 9:56
     */
    private SysApp querySysApp(SysAppRequest sysAppRequest) {
        SysApp sysApp = this.getById(sysAppRequest.getAppId());
        if (ObjectUtil.isNull(sysApp)) {
            throw new ServiceException(AppExceptionEnum.APP_NOT_EXIST);
        }
        return sysApp;
    }

    /**
     * 创建wrapper
     *
     * @author liaoxiting
     * @date 2020/11/6 10:16
     */
    private LambdaQueryWrapper<SysApp> createWrapper(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> queryWrapper = new LambdaQueryWrapper<>();

        // 查询未删除状态的
        queryWrapper.eq(SysApp::getDelFlag, YesOrNotEnum.N.getCode());

        if (ObjectUtil.isEmpty(sysAppRequest)) {
            return queryWrapper;
        }

        // 根据id查询
        queryWrapper.eq(ObjectUtil.isNotEmpty(sysAppRequest.getAppId()), SysApp::getAppId, sysAppRequest.getAppId());

        // 根据名称模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(sysAppRequest.getAppName()), SysApp::getAppName, sysAppRequest.getAppName());

        // 根据编码模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(sysAppRequest.getAppCode()), SysApp::getAppCode, sysAppRequest.getAppCode());

        // 根据激活状态
        queryWrapper.eq(ObjectUtil.isNotEmpty(sysAppRequest.getStatusFlag()), SysApp::getActiveFlag, sysAppRequest.getActiveFlag());

        // 排序
        queryWrapper.orderByAsc(SysApp::getAppSort);

        return queryWrapper;
    }

}

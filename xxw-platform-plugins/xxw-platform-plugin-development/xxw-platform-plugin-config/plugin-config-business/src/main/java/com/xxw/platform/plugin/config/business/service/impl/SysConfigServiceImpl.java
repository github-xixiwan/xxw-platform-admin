package com.xxw.platform.plugin.config.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.callback.ConfigUpdateCallback;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.config.api.ConfigInitCallbackApi;
import com.xxw.platform.plugin.config.api.ConfigInitStrategyApi;
import com.xxw.platform.plugin.config.api.constants.ConfigConstants;
import com.xxw.platform.plugin.config.api.context.ConfigContext;
import com.xxw.platform.plugin.config.api.exception.ConfigException;
import com.xxw.platform.plugin.config.api.exception.enums.ConfigExceptionEnum;
import com.xxw.platform.plugin.config.api.pojo.ConfigInitItem;
import com.xxw.platform.plugin.config.api.pojo.ConfigInitRequest;
import com.xxw.platform.plugin.config.business.entity.SysConfig;
import com.xxw.platform.plugin.config.business.mapper.SysConfigMapper;
import com.xxw.platform.plugin.config.business.pojo.InitConfigGroup;
import com.xxw.platform.plugin.config.business.pojo.InitConfigResponse;
import com.xxw.platform.plugin.config.business.pojo.param.SysConfigParam;
import com.xxw.platform.plugin.config.business.service.SysConfigService;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.file.api.constants.FileConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ??????????????????service???????????????
 *
 * @author liaoxiting
 * @date 2020/4/14 11:16
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysConfigParam sysConfigParam) {

        // 1.????????????
        SysConfig sysConfig = new SysConfig();
        BeanUtil.copyProperties(sysConfigParam, sysConfig);
        sysConfig.setStatusFlag(StatusEnum.ENABLE.getCode());

        // 2.???????????????
        this.save(sysConfig);

        // 3.????????????context
        ConfigContext.me().putConfig(sysConfigParam.getConfigCode(), sysConfigParam.getConfigValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysConfigParam sysConfigParam) {

        // 1.??????id????????????
        SysConfig sysConfig = this.querySysConfig(sysConfigParam);
        if (sysConfig == null) {
            String userTip = StrUtil.format(ConfigExceptionEnum.CONFIG_NOT_EXIST.getUserTip(), "id: " + sysConfigParam.getConfigId());
            throw new ConfigException(ConfigExceptionEnum.CONFIG_NOT_EXIST, userTip);
        }

        // 2.????????????????????????
        if (YesOrNotEnum.Y.getCode().equals(sysConfig.getSysFlag())) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_SYS_CAN_NOT_DELETE);
        }

        // 3.????????????????????????
        sysConfig.setStatusFlag(StatusEnum.DISABLE.getCode());
        sysConfig.setDelFlag(YesOrNotEnum.Y.getCode());
        this.updateById(sysConfig);

        // 4.????????????context
        ConfigContext.me().deleteConfig(sysConfigParam.getConfigCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysConfigParam sysConfigParam) {

        // 1.??????id??????????????????
        SysConfig sysConfig = this.querySysConfig(sysConfigParam);

        // 2.???????????????????????????
        BeanUtil.copyProperties(sysConfigParam, sysConfig);
        // ??????????????????????????????????????????????????????
        sysConfig.setStatusFlag(null);

        // 3.????????????
        this.updateById(sysConfig);

        // 4.??????????????????context
        ConfigContext.me().putConfig(sysConfigParam.getConfigCode(), sysConfigParam.getConfigValue());

        // 5.???????????????????????????
        try {
            Map<String, ConfigUpdateCallback> beansOfType = SpringUtil.getBeansOfType(ConfigUpdateCallback.class);
            for (ConfigUpdateCallback value : beansOfType.values()) {
                value.configUpdate(sysConfig.getConfigCode(), sysConfig.getConfigValue());
            }
        } catch (Exception e) {
            // ???????????????Bean?????????
        }
    }

    @Override
    public SysConfig detail(SysConfigParam sysConfigParam) {
        return this.querySysConfig(sysConfigParam);
    }

    @Override
    public PageResult<SysConfig> findPage(SysConfigParam sysConfigParam) {
        LambdaQueryWrapper<SysConfig> wrapper = createWrapper(sysConfigParam);
        Page<SysConfig> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    public List<SysConfig> findList(SysConfigParam sysConfigParam) {
        LambdaQueryWrapper<SysConfig> wrapper = createWrapper(sysConfigParam);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initConfig(ConfigInitRequest configInitRequest) {

        if (configInitRequest == null || configInitRequest.getSysConfigs() == null) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_INIT_ERROR);
        }

        // ?????????????????????????????????????????????????????????
        LambdaQueryWrapper<SysConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysConfig::getConfigCode, RuleConstants.SYSTEM_CONFIG_INIT_FLAG_NAME);
        SysConfig tempSysConfig = this.getOne(lambdaQueryWrapper, false);
        String alreadyInit = tempSysConfig.getConfigValue();
        if (Convert.toBool(alreadyInit)) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_INIT_ALREADY);
        }

        // ?????????????????????????????????????????????
        Map<String, ConfigInitCallbackApi> beans = SpringUtil.getBeansOfType(ConfigInitCallbackApi.class);

        // ???????????????????????????
        if (ObjectUtil.isNotNull(beans)) {
            for (ConfigInitCallbackApi initCallbackApi : beans.values()) {
                initCallbackApi.initBefore();
            }
        }

        // ????????????????????????????????????
        Map<String, String> sysConfigs = configInitRequest.getSysConfigs();
        sysConfigs.put(RuleConstants.SYSTEM_CONFIG_INIT_FLAG_NAME, "true");

        // ?????????????????????????????????????????????????????????
        for (Map.Entry<String, String> entry : sysConfigs.entrySet()) {
            String configCode = entry.getKey();
            String configValue = entry.getValue();

            // ??????????????????????????????
            LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysConfig::getConfigCode, configCode);
            SysConfig sysConfig = this.getOne(wrapper, false);
            if (sysConfig == null) {
                continue;
            }
            sysConfig.setConfigValue(configValue);
            this.updateById(sysConfig);

            // ????????????
            ConfigContext.me().putConfig(configCode, configValue);
        }

        // ???????????????????????????
        if (ObjectUtil.isNotNull(beans)) {
            for (ConfigInitCallbackApi initCallbackApi : beans.values()) {
                initCallbackApi.initAfter();
            }
        }
    }

    @Override
    public Boolean getInitConfigFlag() {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigCode, RuleConstants.SYSTEM_CONFIG_INIT_FLAG_NAME);
        SysConfig sysConfig = this.getOne(wrapper, false);

        // ??????????????????????????????
        if (sysConfig == null) {
            return true;
        } else {
            String configValue = sysConfig.getConfigValue();
            if (StrUtil.isEmpty(configValue)) {
                return true;
            } else {
                return Convert.toBool(sysConfig.getConfigValue());
            }
        }
    }

    @Override
    public InitConfigResponse getInitConfigs() {

        InitConfigResponse initConfigResponse = new InitConfigResponse();
        initConfigResponse.setTitle("????????????????????????");
        initConfigResponse.setDescription("???????????????Xxw????????????????????????????????????????????????url?????????????????????????????????????????????????????????????????????");

        // ??????????????????????????????????????????
        List<InitConfigGroup> configGroupList = new ArrayList<>();
        Map<String, ConfigInitStrategyApi> beans = SpringUtil.getBeansOfType(ConfigInitStrategyApi.class);
        for (ConfigInitStrategyApi value : beans.values()) {
            String title = value.getTitle();
            String description = value.getDescription();
            List<ConfigInitItem> initConfigs = value.getInitConfigs();
            configGroupList.add(new InitConfigGroup(title, description, initConfigs));
        }
        initConfigResponse.setInitConfigGroupList(configGroupList);

        return initConfigResponse;
    }

    @Override
    public String getServerDeployHost() {

        // ??????????????????????????????
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigCode, ConfigConstants.SYS_SERVER_DEPLOY_HOST);
        SysConfig sysConfig = this.getOne(wrapper, false);

        if (sysConfig != null) {
            return sysConfig.getConfigValue();
        } else {
            return FileConstants.DEFAULT_SERVER_DEPLOY_HOST;
        }
    }

    /**
     * ????????????????????????
     *
     * @author liaoxiting
     * @date 2020/4/14 11:19
     */
    public SysConfig querySysConfig(SysConfigParam sysConfigParam) {
        SysConfig sysConfig = this.getById(sysConfigParam.getConfigId());
        if (ObjectUtil.isEmpty(sysConfig) || sysConfig.getDelFlag().equals(YesOrNotEnum.Y.getCode())) {
            String userTip = StrUtil.format(ConfigExceptionEnum.CONFIG_NOT_EXIST.getUserTip(), "id: " + sysConfigParam.getConfigId());
            throw new ConfigException(ConfigExceptionEnum.CONFIG_NOT_EXIST, userTip);
        }
        return sysConfig;
    }

    /**
     * ??????wrapper
     *
     * @author liaoxiting
     * @date 2020/11/6 10:16
     */
    private LambdaQueryWrapper<SysConfig> createWrapper(SysConfigParam sysConfigParam) {
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();

        // ??????????????????
        queryWrapper.ne(SysConfig::getDelFlag, YesOrNotEnum.Y.getCode());

        // ????????????????????????????????????????????????
        queryWrapper.orderByDesc(SysConfig::getGroupCode);

        if (ObjectUtil.isEmpty(sysConfigParam)) {
            return queryWrapper;
        }

        String configName = sysConfigParam.getConfigName();
        String configCode = sysConfigParam.getConfigCode();
        String groupCode = sysConfigParam.getGroupCode();

        // ??????????????????????????????????????????????????????
        queryWrapper.like(ObjectUtil.isNotEmpty(configName), SysConfig::getConfigName, configName);

        // ??????????????????????????????????????????????????????????????????
        queryWrapper.like(ObjectUtil.isNotEmpty(configCode), SysConfig::getConfigCode, configCode);

        // ???????????????????????????????????????????????????
        queryWrapper.eq(ObjectUtil.isNotEmpty(groupCode), SysConfig::getGroupCode, groupCode);

        return queryWrapper;
    }

}

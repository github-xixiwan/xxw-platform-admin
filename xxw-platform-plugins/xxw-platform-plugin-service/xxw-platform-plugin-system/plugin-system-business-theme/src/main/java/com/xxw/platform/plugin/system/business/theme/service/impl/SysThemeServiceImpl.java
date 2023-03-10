package com.xxw.platform.plugin.system.business.theme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.callback.ConfigUpdateCallback;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.config.api.constants.ConfigConstants;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.file.api.FileInfoApi;
import com.xxw.platform.plugin.file.api.pojo.AntdvFileInfo;
import com.xxw.platform.plugin.file.api.pojo.request.SysFileInfoRequest;
import com.xxw.platform.plugin.file.business.service.SysFileInfoService;
import com.xxw.platform.plugin.system.api.ThemeServiceApi;
import com.xxw.platform.plugin.system.api.constants.SystemConstants;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.theme.SysThemeExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeDTO;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeRequest;
import com.xxw.platform.plugin.system.business.theme.entity.SysTheme;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplate;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplateField;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplateRel;
import com.xxw.platform.plugin.system.business.theme.enums.ThemeFieldTypeEnum;
import com.xxw.platform.plugin.system.business.theme.factory.DefaultThemeFactory;
import com.xxw.platform.plugin.system.business.theme.mapper.SysThemeMapper;
import com.xxw.platform.plugin.system.business.theme.pojo.DefaultTheme;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeService;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeTemplateFieldService;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeTemplateRelService;
import com.xxw.platform.plugin.system.business.theme.service.SysThemeTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ????????????service???????????????
 *
 * @author liaoxiting
 * @date 2021/12/17 16:17
 */
@Service
@Slf4j
public class SysThemeServiceImpl extends ServiceImpl<SysThemeMapper, SysTheme> implements SysThemeService, ThemeServiceApi, ConfigUpdateCallback {

    @Resource
    private SysThemeTemplateService sysThemeTemplateService;

    @Resource
    private SysThemeTemplateFieldService sysThemeTemplateFieldService;

    @Resource
    private SysFileInfoService sysFileInfoService;

    @Resource
    private FileInfoApi fileInfoApi;

    @Resource(name = "themeCacheApi")
    private CacheOperatorApi<DefaultTheme> themeCacheApi;

    @Resource
    private SysThemeTemplateRelService sysThemeTemplateRelService;

    @Override
    public void add(SysThemeRequest sysThemeRequest) {
        // ??????????????????
        SysThemeTemplate sysThemeTemplate = sysThemeTemplateService.getById(sysThemeRequest.getTemplateId());

        // ???????????????????????????????????????????????????????????????
        if (YesOrNotEnum.N.getCode().equals(sysThemeTemplate.getStatusFlag().toString())) {
            throw new SystemModularException(SysThemeExceptionEnum.THEME_TEMPLATE_IS_DISABLE);
        }

        SysTheme sysTheme = new SysTheme();

        // ????????????
        BeanUtil.copyProperties(sysThemeRequest, sysTheme);

        // ????????????????????????-??????N
        sysTheme.setStatusFlag(YesOrNotEnum.N.getCode().charAt(0));

        this.save(sysTheme);
    }

    @Override
    public void del(SysThemeRequest sysThemeRequest) {
        SysTheme sysTheme = this.querySysThemeById(sysThemeRequest);

        // ?????????????????????????????????
        if (YesOrNotEnum.Y.getCode().equals(sysTheme.getStatusFlag().toString())) {
            throw new SystemModularException(SysThemeExceptionEnum.THEME_NOT_ALLOW_DELETE);
        }

        // ?????????????????????
        String themeValue = sysTheme.getThemeValue();
        Map<String, String> themeMap = JSONObject.parseObject(themeValue, new TypeReference<Map<String, String>>() {});

        // ??????map???key
        List<String> themeKeys = new ArrayList<>(themeMap.keySet());

        // ???????????????????????????
        List<String> fileNames = new ArrayList<>();
        if (themeKeys.size() > 0) {
            LambdaQueryWrapper<SysThemeTemplateField> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysThemeTemplateField::getFieldCode, themeKeys).eq(SysThemeTemplateField::getFieldType, ThemeFieldTypeEnum.FILE.getCode())
                    .select(SysThemeTemplateField::getFieldCode);
            List<SysThemeTemplateField> sysThemeTemplateFields = sysThemeTemplateFieldService.list(queryWrapper);
            fileNames = sysThemeTemplateFields.stream().map(SysThemeTemplateField::getFieldCode).collect(Collectors.toList());
        }
        // ????????????
        if (fileNames.size() > 0) {
            for (String themeKey : themeKeys) {
                String themeValueStr = themeMap.get(themeKey);
                for (String fileName : fileNames) {
                    if (StrUtil.isNotBlank(themeKey) && StrUtil.isNotBlank(fileName) && themeKey.equals(fileName)) {
                        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
                        sysFileInfoRequest.setFileId(Long.parseLong(themeValueStr));
                        sysFileInfoService.deleteReally(sysFileInfoRequest);
                    }
                }
            }
        }

        this.removeById(sysTheme);

        // ??????????????????
        this.clearThemeCache();
    }

    @Override
    public void edit(SysThemeRequest sysThemeRequest) {
        SysTheme sysTheme = new SysTheme();

        // ????????????
        BeanUtil.copyProperties(sysThemeRequest, sysTheme);

        this.updateById(sysTheme);

        // ??????????????????
        this.clearThemeCache();
    }

    @Override
    public PageResult<SysThemeDTO> findPage(SysThemeRequest sysThemeRequest) {
        LambdaQueryWrapper<SysTheme> queryWrapper = new LambdaQueryWrapper<>();
        // ??????????????????????????????
        queryWrapper.like(StrUtil.isNotBlank(sysThemeRequest.getThemeName()), SysTheme::getThemeName, sysThemeRequest.getThemeName());

        Page<SysTheme> page = page(PageFactory.defaultPage(), queryWrapper);

        List<SysThemeDTO> sysThemeDTOList = new ArrayList<>();
        for (SysTheme record : page.getRecords()) {
            SysThemeDTO sysThemeDTO = new SysThemeDTO();
            BeanUtil.copyProperties(record, sysThemeDTO);
            SysThemeTemplate sysThemeTemplate = sysThemeTemplateService.getById(record.getTemplateId());
            sysThemeDTO.setTemplateName(sysThemeTemplate.getTemplateName());
            sysThemeDTOList.add(sysThemeDTO);
        }

        return PageResultFactory.createPageResult(sysThemeDTOList, page.getTotal(), Integer.valueOf(String.valueOf(page.getSize())), Integer.valueOf(String.valueOf(page.getCurrent())));
    }

    @Override
    public SysTheme detail(SysThemeRequest sysThemeRequest) {
        SysTheme sysTheme = this.querySysThemeById(sysThemeRequest);

        // ????????????????????????
        String themeValueJson = sysTheme.getThemeValue();
        JSONObject jsonObject = JSON.parseObject(themeValueJson);
        sysTheme.setDynamicForm(jsonObject);

        // ??????????????????????????????????????????????????????????????????????????????????????????
        HashMap<String, AntdvFileInfo[]> tempFileList = new HashMap<>();
        for (Map.Entry<String, Object> keyValues : jsonObject.entrySet()) {
            String key = keyValues.getKey();
            String value = jsonObject.getString(key);
            // ???????????????????????????
            boolean keyFileFlag = sysThemeTemplateFieldService.getKeyFileFlag(key);
            if (keyFileFlag) {
                AntdvFileInfo antdvFileInfo = this.fileInfoApi.buildAntdvFileInfo(Long.valueOf(value));
                tempFileList.put(key, new AntdvFileInfo[]{antdvFileInfo});
            }
        }

        // ???????????????????????????
        sysTheme.setTempFileList(tempFileList);
        return sysTheme;
    }

    @Override
    public void updateThemeStatus(SysThemeRequest sysThemeRequest) {
        SysTheme sysTheme = this.querySysThemeById(sysThemeRequest);

        // ???????????????????????????????????????
        if (YesOrNotEnum.Y.getCode().equals(sysTheme.getStatusFlag().toString())) {
            throw new SystemModularException(SysThemeExceptionEnum.UNIQUE_ENABLE_NOT_DISABLE);
        } else {
            // ???????????????????????????????????????????????????????????????????????????????????????
            sysTheme.setStatusFlag(YesOrNotEnum.Y.getCode().charAt(0));

            LambdaQueryWrapper<SysTheme> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysTheme::getStatusFlag, YesOrNotEnum.Y.getCode().charAt(0));

            if (this.list().size() > 1) {
                SysTheme theme = getOne(queryWrapper, true);
                theme.setStatusFlag(YesOrNotEnum.N.getCode().charAt(0));
                this.updateById(theme);
            }
        }
        this.updateById(sysTheme);

        // ??????????????????
        this.clearThemeCache();
    }

    @Override
    public DefaultTheme currentThemeInfo(SysThemeRequest sysThemeParam) {

        // ????????????????????????????????????
        DefaultTheme defaultTheme = themeCacheApi.get(SystemConstants.THEME_XXW_PLATFORM);
        if (defaultTheme != null) {
            return defaultTheme;
        }

        // ??????????????????????????????
        DefaultTheme result = this.querySystemTheme();

        // ???????????????????????????id??????????????????url?????????
        this.parseFileUrls(result);

        // ??????????????????????????????
        themeCacheApi.put(SystemConstants.THEME_XXW_PLATFORM, result);

        return result;
    }

    @Override
    public void configUpdate(String code, String value) {
        // ????????????????????????????????????????????????
        if (ConfigConstants.SYS_SERVER_DEPLOY_HOST.equals(code)) {
            clearThemeCache();
        }
    }

    /**
     * ????????????????????????
     *
     * @author liaoxiting
     * @date 2021/12/17 16:30
     */
    private SysTheme querySysThemeById(SysThemeRequest sysThemeRequest) {
        SysTheme sysTheme = this.getById(sysThemeRequest.getThemeId());
        if (ObjectUtil.isNull(sysTheme)) {
            throw new SystemModularException(SysThemeExceptionEnum.THEME_NOT_EXIST);
        }
        return sysTheme;
    }

    /**
     * ??????????????????????????????
     *
     * @author liaoxiting
     * @date 2022/1/11 9:44
     */
    private DefaultTheme querySystemTheme() {
        // ???????????????XXW_PLATFORM???????????????id
        Long defaultTemplateId = getDefaultTemplateId();
        if (defaultTemplateId == null) {
            return DefaultThemeFactory.getSystemDefaultTheme();
        }

        // ??????????????????????????????????????????????????????????????????
        LambdaQueryWrapper<SysTheme> sysThemeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysThemeLambdaQueryWrapper.eq(SysTheme::getTemplateId, defaultTemplateId);
        sysThemeLambdaQueryWrapper.eq(SysTheme::getStatusFlag, YesOrNotEnum.Y.getCode());
        sysThemeLambdaQueryWrapper.orderByDesc(BaseEntity::getCreateTime);
        SysTheme sysTheme = this.getOne(sysThemeLambdaQueryWrapper, false);
        if (sysTheme == null) {
            log.error("?????????????????????????????????XXW_PLATFORM????????????????????????????????????????????????????????????");
            return DefaultThemeFactory.getSystemDefaultTheme();
        }

        // ??????????????????json?????????
        String themeValue = sysTheme.getThemeValue();
        if (StrUtil.isNotBlank(themeValue)) {
            JSONObject jsonObject = JSONObject.parseObject(themeValue);
            return DefaultThemeFactory.parseDefaultTheme(jsonObject);
        } else {
            return DefaultThemeFactory.getSystemDefaultTheme();
        }
    }

    /**
     * ??????????????????????????????????????????id???????????????url
     *
     * @author liaoxiting
     * @date 2022/1/11 11:12
     */
    private DefaultTheme parseFileUrls(DefaultTheme theme) {
        // ???????????????XXW_PLATFORM???????????????id
        Long defaultTemplateId = getDefaultTemplateId();
        if (defaultTemplateId == null) {
            return theme;
        }

        // ?????????????????????????????????????????????
        LambdaQueryWrapper<SysThemeTemplateRel> sysThemeTemplateRelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysThemeTemplateRelLambdaQueryWrapper.eq(SysThemeTemplateRel::getTemplateId, defaultTemplateId);
        List<SysThemeTemplateRel> relList = this.sysThemeTemplateRelService.list(sysThemeTemplateRelLambdaQueryWrapper);

        if (ObjectUtil.isEmpty(relList)) {
            return theme;
        }

        // ????????????????????????????????????
        List<String> fieldCodes = relList.stream().map(SysThemeTemplateRel::getFieldCode).collect(Collectors.toList());

        // ???????????????????????????????????????
        LambdaQueryWrapper<SysThemeTemplateField> sysThemeTemplateFieldLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysThemeTemplateFieldLambdaQueryWrapper.in(SysThemeTemplateField::getFieldCode, fieldCodes);
        sysThemeTemplateFieldLambdaQueryWrapper.eq(SysThemeTemplateField::getFieldType, ThemeFieldTypeEnum.FILE.getCode());
        sysThemeTemplateFieldLambdaQueryWrapper.select(SysThemeTemplateField::getFieldCode);
        List<SysThemeTemplateField> fieldInfoList = this.sysThemeTemplateFieldService.list(sysThemeTemplateFieldLambdaQueryWrapper);

        if (ObjectUtil.isEmpty(fieldInfoList)) {
            return theme;
        }

        // ??????????????????????????????
        List<String> needToParse = fieldInfoList.stream().map(SysThemeTemplateField::getFieldCode).map(StrUtil::toCamelCase).collect(Collectors.toList());

        // ????????????
        Map<String, String> otherConfigs = theme.getOtherConfigs();

        for (String fieldName : needToParse) {
            PropertyDescriptor propertyDescriptor = null;
            try {
                propertyDescriptor = new PropertyDescriptor(fieldName, DefaultTheme.class);
                Method readMethod = propertyDescriptor.getReadMethod();
                String fieldValue = (String) readMethod.invoke(theme);
                if (!StrUtil.isEmpty(fieldValue)) {
                    // ?????????id???????????????url
                    String fileUnAuthUrl = fileInfoApi.getFileUnAuthUrl(Long.valueOf(fieldValue));
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(theme, fileUnAuthUrl);
                }
            } catch (Exception e) {
                log.error("?????????????????????id???url?????????", e);
            }

            // ??????????????????????????????????????????
            for (Map.Entry<String, String> otherItem : otherConfigs.entrySet()) {
                if (fieldName.equals(otherItem.getKey())) {
                    String otherFileId = otherItem.getValue();
                    // ?????????id???????????????url
                    String fileUnAuthUrl = fileInfoApi.getFileUnAuthUrl(Long.valueOf(otherFileId));
                    otherConfigs.put(otherItem.getKey(), fileUnAuthUrl);
                }
            }
        }

        return theme;
    }

    /**
     * ???????????????????????????id
     *
     * @author liaoxiting
     * @date 2022/1/11 11:35
     */
    private Long getDefaultTemplateId() {
        // ???????????????XXW_PLATFORM???????????????id
        LambdaQueryWrapper<SysThemeTemplate> sysThemeTemplateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysThemeTemplateLambdaQueryWrapper.eq(SysThemeTemplate::getTemplateCode, SystemConstants.THEME_XXW_PLATFORM);
        SysThemeTemplate sysThemeTemplate = this.sysThemeTemplateService.getOne(sysThemeTemplateLambdaQueryWrapper, false);
        if (sysThemeTemplate == null) {
            log.error("??????????????????????????????XXW_PLATFORM???????????????????????????????????????????????????");
            return null;
        }
        return sysThemeTemplate.getTemplateId();
    }

    /**
     * ??????????????????
     *
     * @author liaoxiting
     * @date 2022/1/12 12:49
     */
    private void clearThemeCache() {
        themeCacheApi.remove(SystemConstants.THEME_XXW_PLATFORM);
    }

}

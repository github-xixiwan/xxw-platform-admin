package com.xxw.platform.plugin.system.business.theme.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeTemplateRelRequest;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplateRel;

/**
 * 系统主题模板属性关系service接口
 *
 * @author liaoxiting
 * @date 2021/12/17 16:13
 */
public interface SysThemeTemplateRelService extends IService<SysThemeTemplateRel> {

    /**
     * 增加系统主题模板属性关系
     *
     * @author liaoxiting
     * @date 2021/12/24 10:56
     */
    void add(SysThemeTemplateRelRequest sysThemeTemplateRelRequest);

    /**
     * 删除系统主题模板属性关系
     *
     * @author liaoxiting
     * @date 2021/12/24 11:18
     */
    void del(SysThemeTemplateRelRequest sysThemeTemplateRelRequest);
}

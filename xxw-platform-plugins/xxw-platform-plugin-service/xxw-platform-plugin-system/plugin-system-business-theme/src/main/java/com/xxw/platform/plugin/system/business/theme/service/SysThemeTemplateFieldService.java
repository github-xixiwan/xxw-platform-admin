package com.xxw.platform.plugin.system.business.theme.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeTemplateFieldRequest;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplateField;

import java.util.List;

/**
 * 系统主题模板属性service接口
 *
 * @author liaoxiting
 * @date 2021/12/17 10:30
 */
public interface SysThemeTemplateFieldService extends IService<SysThemeTemplateField> {

    /**
     * 增加系统主题模板属性
     *
     * @author liaoxiting
     * @date 2021/12/17 10:47
     */
    void add(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest);

    /**
     * 删除系统主题模板属性
     *
     * @author liaoxiting
     * @date 2021/12/17 11:00
     */
    void del(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest);

    /**
     * 修改系统主题模板属性
     *
     * @author liaoxiting
     * @date 2021/12/17 11:29
     */
    void edit(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest);

    /**
     * 查询系统主题模板属性详情
     *
     * @author liaoxiting
     * @date 2021/12/17 11:47
     */
    SysThemeTemplateField detail(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest);

    /**
     * 查询系统主题模板属性列表
     *
     * @return 分页结果
     * @author liaoxiting
     * @date 2021/12/24 9:29
     */
    PageResult<SysThemeTemplateField> findPage(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest);
    /**
     * 查询系统主题模板属性已有关系列表
     *
     * @author liaoxiting
     * @date 2021/12/24 11:35
     */
    List<SysThemeTemplateField> findRelList(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest);

    /**
     * 查询系统主题模板属性未有关系列表
     *
     * @author liaoxiting
     * @date 2021/12/24 11:49
     */
    List<SysThemeTemplateField> findNotRelList(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest);

    /**
     * 根据字段名，获取该属性是否为文件类型
     *
     * @author liaoxiting
     * @date 2022/1/1 22:24
     */
    boolean getKeyFileFlag(String code);

}

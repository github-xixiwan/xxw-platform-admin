package com.xxw.platform.plugin.system.business.theme.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeTemplateDataDTO;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeTemplateRequest;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplate;

import java.util.List;

/**
 * 系统主题模板service接口
 *
 * @author liaoxiting
 * @date 2021/12/17 13:55
 */
public interface SysThemeTemplateService extends IService<SysThemeTemplate> {

    /**
     * 增加系统主题模板
     *
     * @author liaoxiting
     * @date 2021/12/17 14:04
     */
    void add(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 编辑系统主题模板
     *
     * @author liaoxiting
     * @date 2021/12/17 14:21
     */
    void edit(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 删除系统主题模板
     *
     * @author liaoxiting
     * @date 2021/12/17 14:38
     */
    void del(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 查询系统主题模板列表
     *
     * @return 分页结果
     * @author liaoxiting
     * @date 2021/12/17 14:52
     */
    PageResult<SysThemeTemplate> findPage(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 查询系统诸如提模板列表
     *
     * @author liaoxiting
     * @date 2021/12/29 9:10
     */
    List<SysThemeTemplate> findList(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 修改系统主题模板状态
     *
     * @author liaoxiting
     * @date 2021/12/17 15:03
     */
    void updateTemplateStatus(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 查询系统主题模板详情
     *
     * @author liaoxiting
     * @date 2021/12/17 16:00
     */
    List<SysThemeTemplateDataDTO> detail(SysThemeTemplateRequest sysThemeTemplateRequest);
}

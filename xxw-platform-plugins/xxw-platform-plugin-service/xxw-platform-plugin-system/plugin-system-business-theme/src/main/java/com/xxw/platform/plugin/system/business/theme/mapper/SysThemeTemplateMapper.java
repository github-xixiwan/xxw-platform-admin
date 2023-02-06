package com.xxw.platform.plugin.system.business.theme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxw.platform.plugin.system.api.pojo.theme.SysThemeTemplateDataDTO;
import com.xxw.platform.plugin.system.business.theme.entity.SysThemeTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统主题模板mapper接口
 *
 * @author liaoxiting
 * @date 2021/12/17 10:11
 */
public interface SysThemeTemplateMapper extends BaseMapper<SysThemeTemplate> {

    /**
     * 系统主题模板详细查询
     *
     * @author liaoxiting
     * @date 2021/12/17 15:36
     */
    List<SysThemeTemplateDataDTO> sysThemeTemplateDetail(@Param("id") Long id);
}

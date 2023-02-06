package com.xxw.platform.plugin.system.business.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单mapper接口
 *
 * @author liaoxiting
 * @date 2020/3/13 16:05
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 获取一些菜单的统计信息（只返回名称，路径，图标）
     *
     * @author liaoxiting
     * @date 2022/2/12 19:07
     */
    List<SysMenu> getMenuStatInfoByMenuIds(@Param("menuIds") List<Long> menuIds);

}

package com.xxw.platform.plugin.system.business.menu.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenu;
/**
 * 设置菜单的类型
 *
 * @author liaoxiting
 * @date 2021/7/21 14:36
 */
public class MenuTypeFactory {

    /**
     * 根据sysMenu的参数，进行menu类型的判断
     *
     * @author liaoxiting
     * @date 2021/7/21 14:44
     */
    public static void processMenuType(SysMenu sysMenu, String visible) {

        if (sysMenu.getAntdvVisible() == null) {
            sysMenu.setAntdvVisible(YesOrNotEnum.N.getCode());
        }

        if (sysMenu.getLayuiVisible() == null) {
            sysMenu.setLayuiVisible(YesOrNotEnum.N.getCode());
        }

        if (ObjectUtil.isNotEmpty(sysMenu.getAntdvIcon())
                || ObjectUtil.isNotEmpty(sysMenu.getAntdvComponent())
                || ObjectUtil.isNotEmpty(sysMenu.getAntdvComponent())
                || ObjectUtil.isNotEmpty(sysMenu.getAntdvComponent())
                || ObjectUtil.isNotEmpty(sysMenu.getAntdvComponent())
                || ObjectUtil.isNotEmpty(sysMenu.getAntdvComponent())
                || ObjectUtil.isNotEmpty(sysMenu.getAntdvComponent())) {
            if (StrUtil.isEmpty(visible)) {
                sysMenu.setAntdvVisible(YesOrNotEnum.Y.getCode());
            } else {
                sysMenu.setAntdvVisible(visible);
            }
        }

        if (ObjectUtil.isNotEmpty(sysMenu.getAntdvIcon())
                || ObjectUtil.isNotEmpty(sysMenu.getLayuiIcon())
                || ObjectUtil.isNotEmpty(sysMenu.getLayuiPath())) {
            if (StrUtil.isEmpty(visible)) {
                sysMenu.setLayuiVisible(YesOrNotEnum.Y.getCode());
            } else {
                sysMenu.setLayuiVisible(visible);
            }
        }
    }

}

package com.xxw.platform.plugin.system.business.menu.factory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.system.business.menu.constants.MenuButtonConstant;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenuButton;

import java.util.List;
import java.util.Locale;

/**
 * 组装菜单按钮
 *
 * @author liaoxiting
 * @date 2021/1/27 16:22
 */
public class MenuButtonFactory {

    /**
     * 生成系统默认菜单按钮
     *
     * @param menuId 菜单id
     * @return 系统默认菜单按钮集合
     * @author liaoxiting
     * @date 2021/1/27 15:36
     */
    public static List<SysMenuButton> createSystemDefaultButton(Long menuId, String menuName, String menuCode) {

        List<SysMenuButton> sysMenuButtonList = CollUtil.newArrayList();

        // 菜单编码加下划线
        if (StrUtil.isNotBlank(menuCode)) {
            menuCode = menuCode.toUpperCase(Locale.ROOT) + "_";
        }

        // 菜单名称添加下划线
        if (StrUtil.isNotBlank(menuName)) {
            menuName = menuName + "_";
        }

        // 新增按钮
        SysMenuButton addButton = new SysMenuButton();
        addButton.setMenuId(menuId);
        addButton.setButtonCode(menuCode + MenuButtonConstant.DEFAULT_ADD_BUTTON_CODE);
        addButton.setButtonName(menuName + MenuButtonConstant.DEFAULT_ADD_BUTTON_NAME);
        addButton.setDelFlag(YesOrNotEnum.N.getCode());
        sysMenuButtonList.add(addButton);

        // 删除按钮
        SysMenuButton delButton = new SysMenuButton();
        delButton.setMenuId(menuId);
        delButton.setButtonCode(menuCode + MenuButtonConstant.DEFAULT_DEL_BUTTON_CODE);
        delButton.setButtonName(menuName + MenuButtonConstant.DEFAULT_DEL_BUTTON_NAME);
        delButton.setDelFlag(YesOrNotEnum.N.getCode());
        sysMenuButtonList.add(delButton);

        // 修改按钮
        SysMenuButton updateButton = new SysMenuButton();
        updateButton.setMenuId(menuId);
        updateButton.setButtonCode(menuCode + MenuButtonConstant.DEFAULT_UPDATE_BUTTON_CODE);
        updateButton.setButtonName(menuName + MenuButtonConstant.DEFAULT_UPDATE_BUTTON_NAME);
        updateButton.setDelFlag(YesOrNotEnum.N.getCode());
        sysMenuButtonList.add(updateButton);

        // 查询按钮
        SysMenuButton searchButton = new SysMenuButton();
        searchButton.setMenuId(menuId);
        searchButton.setButtonCode(menuCode + MenuButtonConstant.DEFAULT_SEARCH_BUTTON_CODE);
        searchButton.setButtonName(menuName + MenuButtonConstant.DEFAULT_SEARCH_BUTTON_NAME);
        searchButton.setDelFlag(YesOrNotEnum.N.getCode());
        sysMenuButtonList.add(searchButton);

        return sysMenuButtonList;
    }

}

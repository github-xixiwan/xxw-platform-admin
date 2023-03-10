package com.xxw.platform.plugin.system.business.menu.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.frame.common.constants.TreeConstants;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.tree.factory.DefaultTreeBuildFactory;
import com.xxw.platform.plugin.system.api.AppServiceApi;
import com.xxw.platform.plugin.system.api.pojo.app.SysAppResult;
import com.xxw.platform.plugin.system.api.pojo.menu.MenuAndButtonTreeResponse;
import com.xxw.platform.plugin.system.api.pojo.menu.antd.AntdMenuSelectTreeNode;
import com.xxw.platform.plugin.system.api.pojo.menu.antd.AntdSysMenuDTO;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleMenuButtonDTO;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleMenuDTO;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenu;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenuButton;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 针对于antd vue版本的前端菜单的组装
 *
 * @author liaoxiting
 * @date 2020/12/30 20:11
 */
public class AntdMenusFactory {

    /**
     * 组装antdv用的获取所有菜单列表详情
     *
     * @param appSortedMenus 按应用排序过的菜单集合
     * @param appNames       排序过的应用名称
     * @author liaoxiting
     * @date 2021/1/7 18:17
     */
    public static List<AntdSysMenuDTO> createTotalMenus(Map<String, List<SysMenu>> appSortedMenus, List<String> appNames) {

        // 创建应用级别的菜单集合
        ArrayList<AntdSysMenuDTO> appSortedAntdMenus = new ArrayList<>();

        // 创建其他应用的菜单
        for (Map.Entry<String, List<SysMenu>> entry : appSortedMenus.entrySet()) {
            // 创建顶层应用菜单
            AntdSysMenuDTO rootAppMenu = createRootAppMenu(entry.getKey());
            List<SysMenu> treeStructMenu = new DefaultTreeBuildFactory<SysMenu>(TreeConstants.DEFAULT_PARENT_ID.toString()).doTreeBuild(entry.getValue());
            List<AntdSysMenuDTO> antdSysMenuDTOS = doModelTransfer(treeStructMenu);

            // 更新顶层应用级别的菜单
            rootAppMenu.setChildren(antdSysMenuDTOS);
            appSortedAntdMenus.add(rootAppMenu);
        }

        // 更新排序
        if (ObjectUtil.isEmpty(appNames)) {
            return appSortedAntdMenus;
        }
        appSortedAntdMenus.sort((antdSysMenuDTO, antdSysMenuDTO2) -> {
            int one = appNames.indexOf(antdSysMenuDTO.getTitle());
            int two = appNames.indexOf(antdSysMenuDTO2.getTitle());
            return Integer.compare(one, two);
        });

        return appSortedAntdMenus;
    }

    /**
     * menu实体转化为菜单树节点
     *
     * @author liaoxiting
     * @date 2020/11/23 21:54
     */
    public static AntdMenuSelectTreeNode parseMenuBaseTreeNode(SysMenu sysMenu) {
        AntdMenuSelectTreeNode menuTreeNode = new AntdMenuSelectTreeNode();
        menuTreeNode.setId(sysMenu.getMenuId());
        menuTreeNode.setParentId(sysMenu.getMenuParentId());
        menuTreeNode.setValue(String.valueOf(sysMenu.getMenuId()));
        menuTreeNode.setTitle(sysMenu.getMenuName());
        menuTreeNode.setWeight(sysMenu.getMenuSort());
        return menuTreeNode;
    }

    /**
     * 添加根节点
     *
     * @author liaoxiting
     * @date 2021/4/16 15:52
     */
    public static AntdMenuSelectTreeNode createRootNode() {
        AntdMenuSelectTreeNode antdMenuSelectTreeNode = new AntdMenuSelectTreeNode();
        antdMenuSelectTreeNode.setId(-1L);
        antdMenuSelectTreeNode.setParentId(-2L);
        antdMenuSelectTreeNode.setTitle("根节点");
        antdMenuSelectTreeNode.setValue(String.valueOf(antdMenuSelectTreeNode.getId()));
        antdMenuSelectTreeNode.setWeight(new BigDecimal(-1));
        return antdMenuSelectTreeNode;
    }

    /**
     * 填充叶子节点的标识
     *
     * @author liaoxiting
     * @date 2021/8/8 15:22
     */
    public static void fillLeafFlag(List<SysMenu> sysMenuList) {
        for (SysMenu sysMenu : sysMenuList) {
            sysMenu.setLeafFlag(true);

            // 判断这个节点下面有没有节点
            for (SysMenu tempMenu : sysMenuList) {
                if (tempMenu.getMenuPids().contains("[" + sysMenu.getMenuId() + "]")) {
                    sysMenu.setLeafFlag(false);
                }
            }
        }
    }

    /**
     * 菜单集合转化成角色分配菜单的集合
     *
     * @author liaoxiting
     * @date 2021/8/10 22:56
     */
    public static List<MenuAndButtonTreeResponse> parseMenuAndButtonTreeResponse(List<SysMenu> sysMenuList, List<SysRoleMenuDTO> roleBindMenus) {
        ArrayList<MenuAndButtonTreeResponse> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(sysMenuList)) {
            return result;
        }

        for (SysMenu sysMenu : sysMenuList) {
            MenuAndButtonTreeResponse menuAndButtonTreeResponse = new MenuAndButtonTreeResponse();
            menuAndButtonTreeResponse.setId(sysMenu.getMenuId());
            menuAndButtonTreeResponse.setName(sysMenu.getMenuName());
            menuAndButtonTreeResponse.setCode(sysMenu.getMenuCode());
            menuAndButtonTreeResponse.setPid(sysMenu.getMenuParentId());
            menuAndButtonTreeResponse.setChecked(false);

            if (ObjectUtil.isNotEmpty(roleBindMenus)) {
                for (SysRoleMenuDTO roleBindMenu : roleBindMenus) {
                    if (roleBindMenu.getMenuId().equals(sysMenu.getMenuId())) {
                        menuAndButtonTreeResponse.setChecked(true);
                    }
                }
            }

            result.add(menuAndButtonTreeResponse);
        }

        return result;
    }

    /**
     * 菜单集合转化成角色分配菜单的集合
     * <p>
     * 转化过程中包含menu的子集
     *
     * @author liaoxiting
     * @date 2022/9/28 16:42
     */
    public static List<MenuAndButtonTreeResponse> parseMenuAndButtonTreeResponseWithChildren(List<SysMenu> sysMenuList, List<SysRoleMenuDTO> roleBindMenus) {

        // 先转化第一层级的菜单
        List<MenuAndButtonTreeResponse> menuAndButtonTreeResponses = parseMenuAndButtonTreeResponse(sysMenuList, roleBindMenus);

        // 遍历所有菜单，查看是否有二级菜单
        for (SysMenu sysMenu : sysMenuList) {

            // 如果存在二级菜单，则继续进行转化
            if (ObjectUtil.isNotEmpty(sysMenu.getChildren())) {
                for (MenuAndButtonTreeResponse menuAndButtonTreeRespons : menuAndButtonTreeResponses) {
                    if (sysMenu.getMenuId().equals(menuAndButtonTreeRespons.getId())) {
                        List<MenuAndButtonTreeResponse> subLevelItems = parseMenuAndButtonTreeResponse(sysMenu.getChildren(), roleBindMenus);
                        menuAndButtonTreeRespons.setChildren(subLevelItems);
                    }
                }
            }
        }

        return menuAndButtonTreeResponses;
    }

    /**
     * 菜单集合转化成角色分配菜单的集合
     *
     * @author liaoxiting
     * @date 2021/8/10 22:56
     */
    public static void fillButtons(List<MenuAndButtonTreeResponse> sysMenuList, List<SysMenuButton> buttonList, List<SysRoleMenuButtonDTO> roleMenuButtonList) {
        for (MenuAndButtonTreeResponse menuAndButtonTreeResponse : sysMenuList) {
            if (ObjectUtil.isEmpty(buttonList)) {
                continue;
            }

            ArrayList<MenuAndButtonTreeResponse> menuButtonList = new ArrayList<>();

            for (SysMenuButton sysMenuButton : buttonList) {
                if (menuAndButtonTreeResponse.getId().equals(sysMenuButton.getMenuId())) {
                    MenuAndButtonTreeResponse buttonInfo = new MenuAndButtonTreeResponse();
                    buttonInfo.setId(sysMenuButton.getButtonId());
                    buttonInfo.setName(sysMenuButton.getButtonName());
                    buttonInfo.setCode(sysMenuButton.getButtonCode());
                    buttonInfo.setChecked(false);

                    if (ObjectUtil.isNotEmpty(roleMenuButtonList)) {
                        for (SysRoleMenuButtonDTO sysRoleMenuButtonDTO : roleMenuButtonList) {
                            if (sysRoleMenuButtonDTO.getButtonId().equals(sysMenuButton.getButtonId())) {
                                buttonInfo.setChecked(true);
                            }
                        }
                    }

                    menuButtonList.add(buttonInfo);
                }
            }

            menuAndButtonTreeResponse.setButtons(menuButtonList);
        }
    }

    /**
     * 将按钮集成到菜单列表里，并且只返回一级结构
     *
     * @param sysMenuList 菜单集合，包含一级菜单，一级菜单内
     * @param buttonList  菜单下的操作权限集合
     * @author liaoxiting
     * @date 2022/9/28 18:00
     */
    public static List<MenuAndButtonTreeResponse> fillButtons(List<SysMenu> sysMenuList, List<SysMenuButton> buttonList) {
        List<MenuAndButtonTreeResponse> result = new ArrayList<>();

        for (SysMenu sysMenu : sysMenuList) {

            MenuAndButtonTreeResponse menuAndButtonTreeResponse = new MenuAndButtonTreeResponse();
            menuAndButtonTreeResponse.setId(sysMenu.getMenuId());
            menuAndButtonTreeResponse.setName(sysMenu.getMenuName());
            menuAndButtonTreeResponse.setCode(sysMenu.getMenuCode());
            menuAndButtonTreeResponse.setPid(sysMenu.getMenuParentId());
            menuAndButtonTreeResponse.setChecked(false);

            // 获取当前菜单下的所有菜单id
            List<SysMenu> children = sysMenu.getChildren();
            Set<Long> totalMenusIds = children.stream().map(SysMenu::getMenuId).collect(Collectors.toSet());
            totalMenusIds.add(sysMenu.getMenuId());

            // 转化按钮的所属菜单id
            ArrayList<MenuAndButtonTreeResponse> buttons = new ArrayList<>();
            for (SysMenuButton sysMenuButton : buttonList) {
                for (Long menuIdItem : totalMenusIds) {
                    if (sysMenuButton.getMenuId().equals(menuIdItem)) {
                        MenuAndButtonTreeResponse buttonItem = new MenuAndButtonTreeResponse();
                        buttonItem.setId(sysMenuButton.getButtonId());
                        buttonItem.setName(sysMenuButton.getButtonName());
                        buttonItem.setCode(sysMenuButton.getButtonCode());
                        buttonItem.setChecked(false);
                        buttons.add(buttonItem);
                    }
                }
            }
            menuAndButtonTreeResponse.setButtons(buttons);
            result.add(menuAndButtonTreeResponse);
        }

        return result;
    }

    /**
     * 获取分类过的用户菜单，返回一个menus数组，并且第一个是激活的应用
     *
     * @author liaoxiting
     * @date 2021/8/24 16:50
     */
    public static Map<String, List<SysMenu>> sortUserMenusByAppCode(List<SysMenu> currentUserMenus) {

        // 根据应用编码分类的菜单，key是应用编码，value是菜单
        HashMap<String, List<SysMenu>> appMenus = new HashMap<>();

        // 将菜单分类
        for (SysMenu currentUserMenu : currentUserMenus) {

            // 获取这个菜单的应用编码
            String appCode = currentUserMenu.getAppCode();

            // 获取该应用已有的菜单集合
            List<SysMenu> sysMenus = appMenus.get(appCode);
            if (sysMenus == null) {
                sysMenus = new ArrayList<>();
            }

            sysMenus.add(currentUserMenu);
            appMenus.put(appCode, sysMenus);
        }

        return appMenus;
    }

    /**
     * 模型转化
     *
     * @author liaoxiting
     * @date 2021/3/23 21:40
     */
    private static List<AntdSysMenuDTO> doModelTransfer(List<SysMenu> sysMenuList) {
        if (ObjectUtil.isEmpty(sysMenuList)) {
            return null;
        } else {
            ArrayList<AntdSysMenuDTO> resultMenus = new ArrayList<>();

            for (SysMenu sysMenu : sysMenuList) {
                AntdSysMenuDTO antdvMenuItem = new AntdSysMenuDTO();
                antdvMenuItem.setTitle(sysMenu.getMenuName());
                antdvMenuItem.setIcon(sysMenu.getAntdvIcon());
                antdvMenuItem.setPath(sysMenu.getAntdvRouter());
                antdvMenuItem.setComponent(sysMenu.getAntdvComponent());
                antdvMenuItem.setHide(YesOrNotEnum.N.getCode().equals(sysMenu.getAntdvVisible()));
                antdvMenuItem.setActive(sysMenu.getAntdvActiveUrl());
                if (ObjectUtil.isNotEmpty(sysMenu.getChildren())) {
                    antdvMenuItem.setChildren(doModelTransfer(sysMenu.getChildren()));
                }
                resultMenus.add(antdvMenuItem);
            }

            return resultMenus;
        }
    }

    /**
     * 创建顶层应用层级的菜单
     *
     * @author liaoxiting
     * @date 2021/8/24 17:23
     */
    private static AntdSysMenuDTO createRootAppMenu(String appCode) {

        AntdSysMenuDTO antdSysMenuDTO = new AntdSysMenuDTO();

        // 获取应用的详细信息
        AppServiceApi appServiceApi = SpringUtil.getBean(AppServiceApi.class);
        SysAppResult appInfoByAppCode = appServiceApi.getAppInfoByAppCode(appCode);
        antdSysMenuDTO.setTitle(appInfoByAppCode.getAppName());
        antdSysMenuDTO.setIcon(appInfoByAppCode.getAppIcon());
        antdSysMenuDTO.setPath("/" + appCode);
        antdSysMenuDTO.setComponent(null);
        antdSysMenuDTO.setHide(false);
        antdSysMenuDTO.setActive(null);

        return antdSysMenuDTO;
    }

    /**
     * 填充按钮的选中标识
     *
     * @author liaoxiting
     * @date 2022/9/28 18:19
     */
    public static List<MenuAndButtonTreeResponse> fillButtonsChecked(List<MenuAndButtonTreeResponse> menuAndButtonTreeResponses, List<SysRoleMenuButtonDTO> roleMenuButtonList) {

        // 遍历所有菜单中的按钮，将选中标识加上
        for (MenuAndButtonTreeResponse menuAndButtonTreeResponse : menuAndButtonTreeResponses) {

            // 获取菜单中所有的按钮
            List<MenuAndButtonTreeResponse> buttons = menuAndButtonTreeResponse.getButtons();

            // 当前菜单中的所有按钮的数量
            int totalSize = buttons.size();

            // 当前菜单中所有已选择的数量
            int totalSelectSize = 0;

            for (MenuAndButtonTreeResponse button : buttons) {
                for (SysRoleMenuButtonDTO sysRoleMenuButtonDTO : roleMenuButtonList) {
                    if (sysRoleMenuButtonDTO.getButtonId().equals(button.getId())) {
                        button.setChecked(true);
                        totalSelectSize++;
                    }
                }
            }

            // 如果所有按钮都选中行，则设置菜单的选中标识
            if (totalSelectSize == totalSize && totalSelectSize != 0) {
                menuAndButtonTreeResponse.setChecked(true);
            }
        }

        return menuAndButtonTreeResponses;
    }

}

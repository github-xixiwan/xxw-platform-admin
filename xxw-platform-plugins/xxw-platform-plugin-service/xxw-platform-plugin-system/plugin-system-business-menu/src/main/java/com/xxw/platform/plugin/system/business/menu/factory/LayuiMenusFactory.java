package com.xxw.platform.plugin.system.business.menu.factory;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.tree.factory.DefaultTreeBuildFactory;
import com.xxw.platform.frame.common.util.HttpServletUtil;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.i18n.api.constants.TranslationConstants;
import com.xxw.platform.plugin.i18n.api.context.TranslationContext;
import com.xxw.platform.plugin.system.api.AppServiceApi;
import com.xxw.platform.plugin.system.api.pojo.app.SysAppResult;
import com.xxw.platform.plugin.system.api.pojo.menu.layui.LayuiAppIndexMenusVO;
import com.xxw.platform.plugin.system.api.pojo.menu.layui.LayuiIndexMenuTreeNode;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenu;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 针对于layui前端的菜单的组装
 *
 * @author liaoxiting
 * @date 2020/12/27 18:53
 */
public class LayuiMenusFactory {

    /**
     * 创建layui前端首页需要的菜单列表
     *
     * @author liaoxiting
     * @date 2020/12/27 19:07
     */
    public static List<LayuiAppIndexMenusVO> createLayuiAppIndexMenus(List<SysMenu> sysMenuList) {

        String contextPath = HttpServletUtil.getRequest().getContextPath();

        ArrayList<LayuiAppIndexMenusVO> resultList = new ArrayList<>();

        // 找出用户有多少个应用的菜单
        Set<String> appCodes = new HashSet<>();
        for (SysMenu currentUserMenu : sysMenuList) {
            String appCode = currentUserMenu.getAppCode();
            appCodes.add(appCode);
        }

        // 找出每个应用下的所有菜单
        for (String appCode : appCodes) {

            // 找出这个应用下的菜单
            List<SysMenu> appMenus = sysMenuList.stream()
                    .filter(i -> i.getAppCode().equals(appCode))
                    .collect(Collectors.toList());

            // 菜单实体 转化为 layui节点
            ArrayList<LayuiIndexMenuTreeNode> layuiIndexMenuTreeNodes = new ArrayList<>();
            for (SysMenu appMenu : appMenus) {
                LayuiIndexMenuTreeNode layuiIndexMenuTreeNode = new LayuiIndexMenuTreeNode();
                BeanUtil.copyProperties(appMenu, layuiIndexMenuTreeNode);

                // 每个节点的url要加上context-path
                layuiIndexMenuTreeNode.setLayuiPath(contextPath + appMenu.getLayuiPath());

                // 如果当前用户时非中文状态，则翻译菜单
                String tranLanguageCode = LoginContext.me().getLoginUser().getTranLanguageCode();
                if (!RuleConstants.CHINESE_TRAN_LANGUAGE_CODE.equals(tranLanguageCode)) {
                    Map<String, String> tranDictBook = TranslationContext.me().getTranslationDictByLanguage(tranLanguageCode);
                    String translatedName = tranDictBook.get(TranslationConstants.TRAN_CODE_MENU_PREFIX + appMenu.getMenuCode().toUpperCase());
                    if (StrUtil.isNotBlank(translatedName)) {
                        layuiIndexMenuTreeNode.setMenuName(translatedName);
                    }
                }

                layuiIndexMenuTreeNodes.add(layuiIndexMenuTreeNode);
            }

            // 将这些菜单组合成树
            List<LayuiIndexMenuTreeNode> layuiIndexMenuTreeNodeList = new DefaultTreeBuildFactory<LayuiIndexMenuTreeNode>().doTreeBuild(layuiIndexMenuTreeNodes);

            // 将appCode和对应的树包装为实体
            LayuiAppIndexMenusVO layuiAppIndexMenusVO = new LayuiAppIndexMenusVO();
            layuiAppIndexMenusVO.setAppCode(appCode);
            SysAppResult appInfo = getAppNameByAppCode(appCode);
            layuiAppIndexMenusVO.setAppName(appInfo.getAppName());
            layuiAppIndexMenusVO.setAppIcon(appInfo.getAppIcon());
            layuiAppIndexMenusVO.setLayuiIndexMenuTreeNodes(layuiIndexMenuTreeNodeList);
            resultList.add(layuiAppIndexMenusVO);
        }

        return resultList;
    }

    /**
     * 获取应用名称通过应用编码
     *
     * @author liaoxiting
     * @date 2021/1/1 18:09
     */
    private static SysAppResult getAppNameByAppCode(String appCode) {
        AppServiceApi appServiceApi = SpringUtil.getBean(AppServiceApi.class);
        return appServiceApi.getAppInfoByAppCode(appCode);
    }

}

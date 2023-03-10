package com.xxw.platform.plugin.system.business.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.menu.SysMenuButtonExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.menu.SysMenuButtonRequest;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenu;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenuButton;
import com.xxw.platform.plugin.system.business.menu.factory.MenuButtonFactory;
import com.xxw.platform.plugin.system.business.menu.mapper.SysMenuButtonMapper;
import com.xxw.platform.plugin.system.business.menu.service.SysMenuButtonService;
import com.xxw.platform.plugin.system.business.menu.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 系统菜单按钮service接口实现类
 *
 * @author liaoxiting
 * @date 2021/1/9 11:05
 */
@Service
public class SysMenuButtonServiceImpl extends ServiceImpl<SysMenuButtonMapper, SysMenuButton> implements SysMenuButtonService {

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public void add(SysMenuButtonRequest sysMenuButtonRequest) {
        SysMenuButton sysMenuButton = new SysMenuButton();
        BeanUtil.copyProperties(sysMenuButtonRequest, sysMenuButton);
        this.save(sysMenuButton);
    }

    @Override
    public void addDefaultButtons(SysMenuButtonRequest sysMenuButtonRequest) {
        Long menuId = sysMenuButtonRequest.getMenuId();

        // 获取菜单的编码
        SysMenu sysMenu = this.sysMenuService.getById(menuId);

        // 构建菜单的系统默认按钮
        List<SysMenuButton> sysMenuButtonList = MenuButtonFactory.createSystemDefaultButton(menuId, sysMenu.getMenuName(), sysMenu.getMenuCode());
        this.saveBatch(sysMenuButtonList);
    }

    @Override
    public void del(SysMenuButtonRequest sysMenuButtonRequest) {
        SysMenuButton button = this.queryButtonById(sysMenuButtonRequest);

        // 设置为删除状态
        button.setDelFlag(YesOrNotEnum.Y.getCode());
        this.updateById(button);
    }

    @Override
    public void delBatch(SysMenuButtonRequest sysMenuButtonRequest) {
        Set<Long> buttonIds = sysMenuButtonRequest.getButtonIds();
        if (ArrayUtil.isNotEmpty(buttonIds)) {
            // 查询条件
            LambdaQueryWrapper<SysMenuButton> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysMenuButton::getButtonId, buttonIds);
            queryWrapper.eq(SysMenuButton::getDelFlag, YesOrNotEnum.N.getCode());

            // 设置为删除状态
            SysMenuButton entity = new SysMenuButton();
            entity.setDelFlag(YesOrNotEnum.Y.getCode());

            this.update(entity, queryWrapper);
        }
    }

    @Override
    public void edit(SysMenuButtonRequest sysMenuButtonRequest) {
        SysMenuButton button = this.queryButtonById(sysMenuButtonRequest);
        BeanUtil.copyProperties(sysMenuButtonRequest, button);

        // 不更新删除状态
        button.setDelFlag(null);

        // 不更新所属菜单id
        button.setMenuId(null);

        // 不更新菜单code
        button.setButtonCode(null);

        this.updateById(button);
    }

    @Override
    public SysMenuButton detail(SysMenuButtonRequest sysMenuButtonRequest) {
        LambdaQueryWrapper<SysMenuButton> queryWrapper = this.createWrapper(sysMenuButtonRequest);
        return this.getOne(queryWrapper, false);
    }

    @Override
    public PageResult<SysMenuButton> findPage(SysMenuButtonRequest sysMenuButtonRequest) {
        LambdaQueryWrapper<SysMenuButton> wrapper = this.createWrapper(sysMenuButtonRequest);
        Page<SysMenuButton> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }
    @Override
    public void deleteMenuButtonByMenuId(Long menuId) {
        if (ObjectUtil.isNotEmpty(menuId)) {
            // 构建查询条件
            LambdaQueryWrapper<SysMenuButton> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenuButton::getMenuId, menuId);
            queryWrapper.eq(SysMenuButton::getDelFlag, YesOrNotEnum.N.getCode());

            // 设置假删除
            SysMenuButton sysMenuButton = new SysMenuButton();
            sysMenuButton.setDelFlag(YesOrNotEnum.Y.getCode());

            this.update(sysMenuButton, queryWrapper);
        }
    }

    /**
     * 根据主键id获取对象
     *
     * @author liaoxiting
     * @date 2021/1/26 13:28
     */
    private SysMenuButton queryButtonById(SysMenuButtonRequest sysMenuButtonRequest) {
        SysMenuButton button = this.getById(sysMenuButtonRequest.getButtonId());
        if (ObjectUtil.isNull(button)) {
            throw new SystemModularException(SysMenuButtonExceptionEnum.MENU_BUTTON_NOT_EXIST);
        }
        return button;
    }

    /**
     * 实体构建queryWrapper
     *
     * @author liaoxiting
     * @date 2021/1/24 22:03
     */
    private LambdaQueryWrapper<SysMenuButton> createWrapper(SysMenuButtonRequest sysMenuButtonRequest) {
        LambdaQueryWrapper<SysMenuButton> queryWrapper = new LambdaQueryWrapper<>();

        // 逻辑删除
        queryWrapper.eq(SysMenuButton::getDelFlag, YesOrNotEnum.N.getCode());

        if (ObjectUtil.isEmpty(sysMenuButtonRequest)) {
            return queryWrapper;
        }

        Long buttonId = sysMenuButtonRequest.getButtonId();
        Long menuId = sysMenuButtonRequest.getMenuId();
        String buttonName = sysMenuButtonRequest.getButtonName();
        String buttonCode = sysMenuButtonRequest.getButtonCode();

        // SQL条件拼接
        queryWrapper.eq(ObjectUtil.isNotNull(buttonId), SysMenuButton::getButtonId, buttonId);
        queryWrapper.eq(ObjectUtil.isNotNull(menuId), SysMenuButton::getMenuId, menuId);
        queryWrapper.like(ObjectUtil.isNotNull(buttonName), SysMenuButton::getButtonName, buttonName);
        queryWrapper.like(ObjectUtil.isNotNull(buttonCode), SysMenuButton::getButtonCode, buttonCode);

        return queryWrapper;
    }

}

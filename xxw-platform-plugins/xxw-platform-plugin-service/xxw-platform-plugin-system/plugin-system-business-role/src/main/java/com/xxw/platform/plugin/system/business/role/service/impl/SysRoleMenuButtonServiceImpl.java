package com.xxw.platform.plugin.system.business.role.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.enums.DbTypeEnum;
import com.xxw.platform.plugin.db.api.context.DbOperatorContext;
import com.xxw.platform.plugin.system.business.role.entity.SysRoleMenuButton;
import com.xxw.platform.plugin.system.business.role.mapper.SysRoleMenuButtonMapper;
import com.xxw.platform.plugin.system.business.role.service.SysRoleMenuButtonService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色按钮关联 服务实现类
 *
 * @author liaoxiting
 * @date 2021/01/09 11:48
 */
@Service
public class SysRoleMenuButtonServiceImpl extends ServiceImpl<SysRoleMenuButtonMapper, SysRoleMenuButton> implements SysRoleMenuButtonService {

    @Override
    public void batchSaveRoleMenuButton(List<SysRoleMenuButton> roleMenuButtons) {
        DbTypeEnum currentDbType = DbOperatorContext.me().getCurrentDbType();
        if (DbTypeEnum.MYSQL.equals(currentDbType)) {
            List<List<SysRoleMenuButton>> split = ListUtil.split(roleMenuButtons, RuleConstants.DEFAULT_BATCH_INSERT_SIZE);
            for (List<SysRoleMenuButton> roleMenuButtonList : split) {
                this.getBaseMapper().insertBatchSomeColumn(roleMenuButtonList);
            }
        } else {
            this.saveBatch(roleMenuButtons);
        }
    }

}

package com.xxw.platform.plugin.dict.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.dict.api.DictApi;
import com.xxw.platform.plugin.dict.business.entity.SysDict;
import com.xxw.platform.plugin.dict.business.pojo.TreeDictInfo;
import com.xxw.platform.plugin.dict.business.pojo.request.DictRequest;

import java.util.List;

/**
 * 字典详情管理
 *
 * @author liaoxiting
 * @date 2020/10/29 17:43
 */
public interface DictService extends IService<SysDict>, DictApi {

    /**
     * 新增字典
     *
     * @param dictRequest 字典对象
     * @author liaoxiting
     * @date 2020/10/29 17:43
     */
    void add(DictRequest dictRequest);

    /**
     * 删除字典
     *
     * @param dictRequest 字典对象
     * @author liaoxiting
     * @date 2020/10/29 17:43
     */
    void del(DictRequest dictRequest);

    /**
     * 修改字典
     *
     * @param dictRequest 字典对象
     * @author liaoxiting
     * @date 2020/10/29 17:43
     */
    void edit(DictRequest dictRequest);

    /**
     * 查询字典详情
     *
     * @param dictRequest 字典id
     * @return 字典的详情
     * @author liaoxiting
     * @date 2020/10/30 16:15
     */
    SysDict detail(DictRequest dictRequest);

    /**
     * 获取字典列表
     *
     * @param dictRequest 字典对象
     * @return 字典列表
     * @author liaoxiting
     * @date 2020/10/29 18:48
     */
    List<SysDict> findList(DictRequest dictRequest);

    /**
     * 获取字典列表（带分页）
     *
     * @param dictRequest 查询条件
     * @return 带分页的列表
     * @author liaoxiting
     * @date 2020/10/29 18:48
     */
    PageResult<SysDict> findPage(DictRequest dictRequest);

    /**
     * 获取树形字典列表（antdv在用）
     *
     * @param dictRequest 查询条件
     * @return 字典信息列表
     * @author liaoxiting
     * @date 2020/10/29 18:50
     */
    List<TreeDictInfo> getTreeDictList(DictRequest dictRequest);
}

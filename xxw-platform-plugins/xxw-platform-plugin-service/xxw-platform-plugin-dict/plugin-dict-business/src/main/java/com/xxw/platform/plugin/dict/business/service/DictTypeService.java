package com.xxw.platform.plugin.dict.business.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.dict.business.entity.SysDictType;
import com.xxw.platform.plugin.dict.business.pojo.request.DictTypeRequest;

import java.util.List;

/**
 * 字典类型管理
 *
 * @author liaoxiting
 * @date 2020/10/29 18:54
 */
public interface DictTypeService extends IService<SysDictType> {

    /**
     * 添加字典类型
     *
     * @param dictTypeRequest 字典类型请求
     * @author liaoxiting
     * @date 2020/10/29 18:55
     */
    void add(DictTypeRequest dictTypeRequest);

    /**
     * 删除字典类型
     *
     * @param dictTypeRequest 字典类型请求
     * @author liaoxiting
     * @date 2020/10/29 18:55
     */
    void del(DictTypeRequest dictTypeRequest);

    /**
     * 修改字典类型
     *
     * @param dictTypeRequest 字典类型请求
     * @author liaoxiting
     * @date 2020/10/29 18:55
     */
    void edit(DictTypeRequest dictTypeRequest);

    /**
     * 修改字典状态
     *
     * @param dictTypeRequest 字典类型请求
     * @author liaoxiting
     * @date 2020/10/29 18:56
     */
    void editStatus(DictTypeRequest dictTypeRequest);

    /**
     * 查询-详情-按实体对象
     *
     * @param dictTypeRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    SysDictType detail(DictTypeRequest dictTypeRequest);

    /**
     * 获取字典类型列表
     *
     * @param dictTypeRequest 字典类型请求
     * @return 字典类型列表
     * @author liaoxiting
     * @date 2020/10/29 18:55
     */
    List<SysDictType> findList(DictTypeRequest dictTypeRequest);

    /**
     * 获取字典类型列表（带分页）
     *
     * @param dictTypeRequest 字典类型请求
     * @return 字典类型列表
     * @author liaoxiting
     * @date 2020/10/29 18:55
     */
    PageResult<SysDictType> findPage(DictTypeRequest dictTypeRequest);

}

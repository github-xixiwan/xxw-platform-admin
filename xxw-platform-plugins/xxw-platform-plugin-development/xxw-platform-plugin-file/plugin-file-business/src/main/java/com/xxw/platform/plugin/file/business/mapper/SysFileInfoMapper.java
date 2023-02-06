package com.xxw.platform.plugin.file.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxw.platform.plugin.file.api.pojo.request.SysFileInfoRequest;
import com.xxw.platform.plugin.file.api.pojo.response.SysFileInfoListResponse;
import com.xxw.platform.plugin.file.business.entity.SysFileInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件信息表 Mapper 接口
 * </p>
 *
 * @author liaoxiting
 * @date 2020/6/7 22:15
 */
public interface SysFileInfoMapper extends BaseMapper<SysFileInfo> {

    /**
     * 附件列表（有分页）
     *
     * @author liaoxiting
     * @date 2020/12/27 12:57
     */
    List<SysFileInfoListResponse> fileInfoList(@Param("page") Page<SysFileInfoListResponse> page, @Param("sysFileInfoRequest") SysFileInfoRequest sysFileInfoRequest);

}

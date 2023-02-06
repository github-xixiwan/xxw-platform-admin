package com.xxw.platform.plugin.ds.business.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.ds.api.pojo.request.DatabaseInfoRequest;
import com.xxw.platform.plugin.ds.business.entity.DatabaseInfo;
import com.xxw.platform.plugin.ds.business.service.DatabaseInfoService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
/**
 * 数据库信息表控制器
 *
 * @author liaoxiting
 * @date 2020/11/1 22:15
 */
@RestController
@ApiResource(name = "数据源信息管理", resBizType = ResBizTypeEnum.SYSTEM)
public class DatabaseInfoController {

    @Resource
    private DatabaseInfoService databaseInfoService;

    /**
     * 新增数据源
     *
     * @author liaoxiting
     * @date 2020/11/1 22:16
     */
    @PostResource(name = "新增数据源", path = "/databaseInfo/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(BaseRequest.add.class) DatabaseInfoRequest databaseInfoRequest) {
        databaseInfoService.add(databaseInfoRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除数据源
     *
     * @author liaoxiting
     * @date 2020/11/1 22:18
     */
    @PostResource(name = "删除数据源", path = "/databaseInfo/delete")
    @BusinessLog
    public ResponseData<?> del(@RequestBody @Validated(DatabaseInfoRequest.delete.class) DatabaseInfoRequest databaseInfoRequest) {
        databaseInfoService.del(databaseInfoRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑数据源
     *
     * @author liaoxiting
     * @date 2020/11/1 22:16
     */
    @PostResource(name = "编辑数据源", path = "/databaseInfo/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(DatabaseInfoRequest.edit.class) DatabaseInfoRequest databaseInfoRequest) {
        databaseInfoService.edit(databaseInfoRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查询数据源列表（带分页）
     *
     * @author liaoxiting
     * @date 2020/11/1 22:18
     */
    @GetResource(name = "查询数据源列表（带分页）", path = "/databaseInfo/page")
    public ResponseData<PageResult<DatabaseInfo>> findPage(DatabaseInfoRequest databaseInfoRequest) {
        PageResult<DatabaseInfo> pageResult = databaseInfoService.findPage(databaseInfoRequest);
        return new SuccessResponseData<>(pageResult);
    }

    /**
     * 查询所有数据源列表
     *
     * @author liaoxiting
     * @date 2020/11/1 22:18
     */
    @GetResource(name = "查询所有数据源列表", path = "/databaseInfo/list")
    public ResponseData<List<DatabaseInfo>> findList(DatabaseInfoRequest databaseInfoRequest) {
        List<DatabaseInfo> databaseInfos = databaseInfoService.findList(databaseInfoRequest);
        return new SuccessResponseData<>(databaseInfos);
    }

    /**
     * 查询数据源详情
     *
     * @author liaoxiting
     * @date 2021/1/23 20:29
     */
    @GetResource(name = "查询数据源详情", path = "/databaseInfo/detail")
    public ResponseData<DatabaseInfo> detail(@Validated(BaseRequest.detail.class) DatabaseInfoRequest databaseInfoRequest) {
        DatabaseInfo databaseInfo = databaseInfoService.detail(databaseInfoRequest);
        return new SuccessResponseData<>(databaseInfo);
    }

}

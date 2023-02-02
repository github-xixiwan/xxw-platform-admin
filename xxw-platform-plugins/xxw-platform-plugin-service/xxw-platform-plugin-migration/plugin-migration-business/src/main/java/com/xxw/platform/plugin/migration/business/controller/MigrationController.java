package com.xxw.platform.plugin.migration.business.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.migration.api.pojo.MigrationAggregationPOJO;
import com.xxw.platform.plugin.migration.business.pojo.MigrationRequest;
import com.xxw.platform.plugin.migration.business.service.MigrationService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据迁移控制器
 *
 * @author majianguo
 * @date 2021/7/6 17:35
 */
@RestController
@ApiResource(name = "数据迁移控制器", resBizType = ResBizTypeEnum.SYSTEM)
public class MigrationController {

    @Resource
    private MigrationService migrationService;

    /**
     * 获取所有可备份数据列表
     *
     * @return {@link ResponseData}
     * @author majianguo
     * @date 2021/7/6 17:37
     **/
    @GetResource(name = "获取所有可备份数据列表", path = "/dataMigration/getAllMigrationList")
    public ResponseData<List<MigrationRequest>> getAllMigrationList() {
        List<MigrationRequest> migrationRequestList = migrationService.getAllMigrationList();
        return new SuccessResponseData<>(migrationRequestList);
    }

    /**
     * 备份指定数据列表
     *
     * @return {@link import com.xxw.platform.plugin.rule.pojo.response.ResponseData}
     * @author majianguo
     * @date 2021/7/7 11:11
     **/
    @GetResource(name = "备份指定数据列表", path = "/dataMigration/migrationSelectData")
    public ResponseData<String> migrationSelectData(@Validated(MigrationAggregationPOJO.export.class) MigrationAggregationPOJO migrationAggregationPOJO) {
        List<String> res = new ArrayList<>();
        for (String s : migrationAggregationPOJO.getAppAndModuleNameList()) {
            try {
                String decode = URLDecoder.decode(s, "UTF-8");
                res.add(decode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        migrationAggregationPOJO.setAppAndModuleNameList(res);
        String migrationSelectDataStr = migrationService.migrationSelectData(migrationAggregationPOJO);
        return new SuccessResponseData<>(migrationSelectDataStr);
    }

    /**
     * 恢复备份数据
     *
     * @return {@link ResponseData}
     * @author majianguo
     * @date 2021/7/7 11:11
     **/
    @PostResource(name = "恢复备份数据", path = "/dataMigration/restoreData")
    public ResponseData<?> restoreData(@RequestPart("file") MultipartFile file, String type) {
        migrationService.restoreData(file, type);
        return new SuccessResponseData<>();
    }
}

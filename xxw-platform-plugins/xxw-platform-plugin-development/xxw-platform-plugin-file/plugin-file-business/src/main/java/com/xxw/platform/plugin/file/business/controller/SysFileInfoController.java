package com.xxw.platform.plugin.file.business.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.frame.common.util.HttpServletUtil;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.file.api.FileInfoApi;
import com.xxw.platform.plugin.file.api.constants.FileConstants;
import com.xxw.platform.plugin.file.api.pojo.AntdvFileInfo;
import com.xxw.platform.plugin.file.api.pojo.request.SysFileInfoRequest;
import com.xxw.platform.plugin.file.api.pojo.response.SysFileInfoListResponse;
import com.xxw.platform.plugin.file.api.pojo.response.SysFileInfoResponse;
import com.xxw.platform.plugin.file.business.entity.SysFileInfo;
import com.xxw.platform.plugin.file.business.service.SysFileInfoService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件信息管理
 * <p>
 * 该模块简要说明：
 * 1.文件支持版本朔源，每次操作均会产生数据
 * 2.文件支持版本回滚，升级后可选择某一版本进行回退
 * <p>
 * 文件管理接口有两种使用方式：
 * 1.合同文件场景：文件必须保持原样，合同内容升级不影响已签署合同，业务需要关联文件ID<br>
 * 文件升级不会对之前的数据造成影响
 * 2.UI文件场景：文件升级后业务所有关联的文件全部升级，业务需要关联文件CODE<br>
 *
 * @author liaoxiting
 * @date 2020/12/27 13:39
 */
@RestController
@ApiResource(name = "文件信息相关接口", resBizType = ResBizTypeEnum.SYSTEM)
public class SysFileInfoController {

    @Resource
    private SysFileInfoService sysFileInfoService;

    @Resource
    private FileInfoApi fileInfoApi;

    /**
     * 上传文件
     * <p>
     * 支持上传到数据库，参数fileLocation传递5即可
     * <p>
     * fileLocation传递其他值或不传值，不能决定文件上传到本地还是阿里云或其他地方
     *
     * @author liaoxiting
     * @date 2020/12/27 13:17
     */
    @PostResource(name = "上传文件", path = "/sysFileInfo/upload", requiredPermission = false)
    public ResponseData<SysFileInfoResponse> upload(@RequestPart("file") MultipartFile file, @Validated(SysFileInfoRequest.add.class) SysFileInfoRequest sysFileInfoRequest) {
        SysFileInfoResponse fileUploadInfoResult = this.sysFileInfoService.uploadFile(file, sysFileInfoRequest);
        return new SuccessResponseData<>(fileUploadInfoResult);
    }

    /**
     * 富文本tinymce上传文件
     * 需要返回格式
     * //json格式
     * { "location": "folder/sub-folder/new-location.png" }
     *
     * @author liaoxiting
     * @date 2021/1/17 11:17
     */
    @PostResource(name = "上传文件", path = "/sysFileInfo/tinymceUpload", requiredPermission = false)
    public Map<String, String> tinymceUpload(@RequestPart("file") MultipartFile file, SysFileInfoRequest sysFileInfoRequest) {
        Map<String, String> resultMap = new HashMap<>(1);
        sysFileInfoRequest.setSecretFlag(YesOrNotEnum.N.getCode());
        SysFileInfoResponse fileUploadInfoResult = this.sysFileInfoService.uploadFile(file, sysFileInfoRequest);
        resultMap.put("location", FileConstants.FILE_PUBLIC_PREVIEW_URL + "?fileId=" + fileUploadInfoResult.getFileId());
        return resultMap;
    }

    /**
     * 私有文件预览
     *
     * @author liaoxiting
     * @date 2020/11/29 11:29
     */
    @GetResource(name = "私有文件预览", path = FileConstants.FILE_PRIVATE_PREVIEW_URL, requiredPermission = false)
    public void privatePreview(@Validated(SysFileInfoRequest.detail.class) SysFileInfoRequest sysFileInfoRequest) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        sysFileInfoRequest.setSecretFlag(YesOrNotEnum.Y.getCode());
        this.sysFileInfoService.preview(sysFileInfoRequest, response);
    }

    /**
     * 公有文件预览
     *
     * @author liaoxiting
     * @date 2020/12/27 13:17
     */
    @GetResource(name = "公有文件预览", path = FileConstants.FILE_PUBLIC_PREVIEW_URL, requiredPermission = false, requiredLogin = false)
    public void publicPreview(@Validated(SysFileInfoRequest.detail.class) SysFileInfoRequest sysFileInfoRequest) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        sysFileInfoRequest.setSecretFlag(YesOrNotEnum.N.getCode());
        this.sysFileInfoService.preview(sysFileInfoRequest, response);
    }

    /**
     * 通用文件预览，通过传bucket名称和object名称
     *
     * @author liaoxiting
     * @date 2020/11/29 11:29
     */
    @GetResource(name = "文件预览，通过bucketName和objectName", path = FileConstants.FILE_PREVIEW_BY_OBJECT_NAME, requiredPermission = false, requiredLogin = false)
    public void previewByBucketNameObjectName(@Validated(SysFileInfoRequest.previewByObjectName.class) SysFileInfoRequest sysFileInfoRequest) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        sysFileInfoService.previewByBucketAndObjName(sysFileInfoRequest, response);
    }

    /**
     * 私有文件下载
     *
     * @author liaoxiting
     * @date 2020/12/27 13:17
     */
    @GetResource(name = "私有文件下载", path = "/sysFileInfo/privateDownload", requiredPermission = false)
    public void privateDownload(@Validated(SysFileInfoRequest.detail.class) SysFileInfoRequest sysFileInfoRequest) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        sysFileInfoRequest.setSecretFlag(YesOrNotEnum.Y.getCode());
        this.sysFileInfoService.download(sysFileInfoRequest, response);
    }

    /**
     * 公有文件下载
     *
     * @author liaoxiting
     * @date 2020/12/27 13:17
     */
    @GetResource(name = "公有文件下载", path = "/sysFileInfo/publicDownload", requiredLogin = false, requiredPermission = false)
    public void publicDownload(@Validated(SysFileInfoRequest.detail.class) SysFileInfoRequest sysFileInfoRequest) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        sysFileInfoRequest.setSecretFlag(YesOrNotEnum.N.getCode());
        this.sysFileInfoService.download(sysFileInfoRequest, response);
    }

    /**
     * 替换文件
     * <p>
     * 注意：调用本接口之后还需要调用确认接口，本次替换操作才会生效
     *
     * @author liaoxiting
     * @date 2020/12/16 15:34
     */
    @PostResource(name = "替换文件", path = "/sysFileInfo/update", requiredPermission = false)
    public ResponseData<SysFileInfoResponse> update(@RequestPart("file") MultipartFile file, @Validated(SysFileInfoRequest.edit.class) SysFileInfoRequest sysFileInfoRequest) {
        SysFileInfoResponse fileUploadInfoResult = this.sysFileInfoService.updateFile(file, sysFileInfoRequest);
        return new SuccessResponseData<>(fileUploadInfoResult);
    }

    /**
     * 版本回退
     *
     * @author liaoxiting
     * @date 2020/12/16 15:34
     */
    @PostResource(name = "版本回退", path = "/sysFileInfo/versionBack", requiredPermission = false)
    public ResponseData<SysFileInfoResponse> versionBack(@Validated(SysFileInfoRequest.versionBack.class) SysFileInfoRequest sysFileInfoRequest) {
        SysFileInfoResponse fileUploadInfoResult = this.sysFileInfoService.versionBack(sysFileInfoRequest);
        return new SuccessResponseData<>(fileUploadInfoResult);
    }

    /**
     * 根据附件IDS查询附件信息
     *
     * @param fileIds 附件IDS
     * @return 附件返回类
     * @author liaoxiting
     * @date 2020/12/27 13:17
     */
    @GetResource(name = "根据附件IDS查询附件信息", path = "/sysFileInfo/getFileInfoListByFileIds", requiredPermission = false)
    public ResponseData<List<SysFileInfoResponse>> getFileInfoListByFileIds(@RequestParam(value = "fileIds") String fileIds) {
        List<SysFileInfoResponse> list = this.sysFileInfoService.getFileInfoListByFileIds(fileIds);
        return new SuccessResponseData<>(list);
    }

    /**
     * 公有打包下载文件
     *
     * @author liaoxiting
     * @date 2020/12/27 13:17
     */
    @GetResource(name = "公有打包下载文件", path = "/sysFileInfo/publicPackagingDownload", requiredPermission = false, requiredLogin = false)
    public void publicPackagingDownload(@RequestParam(value = "fileIds") String fileIds) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        this.sysFileInfoService.packagingDownload(fileIds, YesOrNotEnum.N.getCode(), response);
    }

    /**
     * 私有打包下载文件
     *
     * @author liaoxiting
     * @date 2020/12/27 13:18
     */
    @GetResource(name = "私有打包下载文件", path = "/sysFileInfo/privatePackagingDownload", requiredPermission = false)
    public void privatePackagingDownload(@RequestParam(value = "fileIds") String fileIds) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        this.sysFileInfoService.packagingDownload(fileIds, YesOrNotEnum.Y.getCode(), response);
    }

    /**
     * 删除文件信息（真删除文件信息）
     *
     * @author liaoxiting
     * @date 2020/11/29 11:19
     */
    @PostResource(name = "删除文件信息（真删除文件信息）", path = "/sysFileInfo/deleteReally", requiredPermission = false)
    @BusinessLog
    public ResponseData<?> deleteReally(@RequestBody @Validated(SysFileInfoRequest.delete.class) SysFileInfoRequest sysFileInfoRequest) {
        this.sysFileInfoService.deleteReally(sysFileInfoRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 分页查询文件信息表
     *
     * @author liaoxiting
     * @date 2020/11/29 11:29
     */
    @GetResource(name = "分页查询文件信息表", path = "/sysFileInfo/fileInfoListPage", requiredPermission = false)
    public ResponseData<PageResult<SysFileInfoListResponse>> fileInfoListPage(SysFileInfoRequest sysFileInfoRequest) {
        return new SuccessResponseData<>(this.sysFileInfoService.fileInfoListPage(sysFileInfoRequest));
    }

    /**
     * 查看详情文件信息表
     *
     * @author liaoxiting
     * @date 2020/11/29 11:29
     */
    @GetResource(name = "查看详情文件信息表", path = "/sysFileInfo/detail", requiredPermission = false)
    public ResponseData<SysFileInfo> detail(@Validated(SysFileInfoRequest.detail.class) SysFileInfoRequest sysFileInfoRequest) {
        return new SuccessResponseData<>(sysFileInfoService.detail(sysFileInfoRequest));
    }

    /**
     * 获取antdv类型的文件详情信息
     *
     * @author liaoxiting
     * @date 2022/3/28 14:37
     */
    @GetResource(name = "获取antdv类型的文件详情信息", path = "/sysFileInfo/getAntdVInfo")
    public ResponseData<AntdvFileInfo> getAntdVInfo(@Validated(SysFileInfoRequest.detail.class) SysFileInfoRequest sysFileInfoRequest) {
        return new SuccessResponseData<>(fileInfoApi.buildAntdvFileInfo(sysFileInfoRequest.getFileId()));
    }

}

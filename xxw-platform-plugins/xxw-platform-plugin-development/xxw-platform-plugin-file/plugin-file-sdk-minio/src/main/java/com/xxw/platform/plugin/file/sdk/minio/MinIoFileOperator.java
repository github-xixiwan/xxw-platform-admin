package com.xxw.platform.plugin.file.sdk.minio;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xxw.platform.frame.common.util.HttpServletUtil;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.file.api.FileOperatorApi;
import com.xxw.platform.plugin.file.api.constants.FileConstants;
import com.xxw.platform.plugin.file.api.enums.BucketAuthEnum;
import com.xxw.platform.plugin.file.api.enums.FileLocationEnum;
import com.xxw.platform.plugin.file.api.exception.FileException;
import com.xxw.platform.plugin.file.api.exception.enums.FileExceptionEnum;
import com.xxw.platform.plugin.file.api.expander.FileConfigExpander;
import com.xxw.platform.plugin.file.api.pojo.props.MinIoProperties;
import io.minio.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * MinIo文件操作客户端
 *
 * @author liaoxiting
 * @date 2020/10/31 10:35
 */
public class MinIoFileOperator implements FileOperatorApi {

    private final Object LOCK = new Object();

    /**
     * 文件ContentType对应关系
     */
    Map<String, String> contentType = new HashMap<>();

    /**
     * MinIo文件操作客户端
     */
    private MinioClient minioClient;

    /**
     * MinIo的配置
     */
    private final MinIoProperties minIoProperties;

    public MinIoFileOperator(MinIoProperties minIoProperties) {
        this.minIoProperties = minIoProperties;
        this.initClient();
    }

    @Override
    public void initClient() {
        String endpoint = minIoProperties.getEndpoint();
        String accessKey = minIoProperties.getAccessKey();
        String secretKey = minIoProperties.getSecretKey();

        // 创建minioClient实例
        try {
            minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        } catch (Exception e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void destroyClient() {
        // empty
    }

    @Override
    public Object getClient() {
        return minioClient;
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void setBucketAcl(String bucketName, BucketAuthEnum bucketAuthEnum) {
        setFileAcl(bucketName, "*", bucketAuthEnum);
    }

    @Override
    public boolean isExistingFile(String bucketName, String key) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(key).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    @Override
    public void storageFile(String bucketName, String key, byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            // 字节数组转字节数组输入流
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(bytes);
            // 获取文件类型
            ByteArrayInputStream tmp = IoUtil.toStream(bytes);
            try {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(key)
                                .stream(tmp, tmp.available(), -1)
                                .build());
            } catch (Exception e) {
                // 组装提示信息
                throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public void storageFile(String bucketName, String key, InputStream inputStream) {
        if (inputStream != null) {
            byte[] bytes = IoUtil.readBytes(inputStream);
            storageFile(bucketName, key, bytes);
        }
    }

    @Override
    public byte[] getFileBytes(String bucketName, String key) {
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(key).build());
            return IoUtil.readBytes(inputStream);
        } catch (Exception e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void setFileAcl(String bucketName, String key, BucketAuthEnum bucketAuthEnum) {
        try {
            if (bucketAuthEnum.equals(BucketAuthEnum.PRIVATE)) {
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(key).build());
            } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ)) {
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(key).build());
            } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ_WRITE)) {
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(key).build());
            } else if (bucketAuthEnum.equals(BucketAuthEnum.MINIO_WRITE_ONLY)) {
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(key).build());
            }
        } catch (Exception e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void copyFile(String originBucketName, String originFileKey, String newBucketName, String newFileKey) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .source(CopySource.builder().bucket(originBucketName).object(originFileKey).build())
                            .bucket(newBucketName)
                            .object(newFileKey)
                            .build());
        } catch (Exception e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public String getFileAuthUrl(String bucketName, String key, Long timeoutMillis) {

        // 获取登录用户的token
        String token = LoginContext.me().getToken();

        // 获取context-path
        String contextPath = HttpServletUtil.getRequest().getContextPath();

        return FileConfigExpander.getServerDeployHost() + contextPath + FileConstants.FILE_PREVIEW_BY_OBJECT_NAME + "?fileBucket=" + bucketName + "&fileObjectName=" + key + "&token=" + token;

    }

    @Override
    public String getFileUnAuthUrl(String bucketName, String key) {
        // 获取context-path
        String contextPath = HttpServletUtil.getRequest().getContextPath();

        return FileConfigExpander.getServerDeployHost() + contextPath + FileConstants.FILE_PREVIEW_BY_OBJECT_NAME + "?fileBucket=" + bucketName + "&fileObjectName=" + key;
    }

    @Override
    public void deleteFile(String bucketName, String key) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .build());
        } catch (Exception e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }

    }

    @Override
    public FileLocationEnum getFileLocationEnum() {
        return FileLocationEnum.MINIO;
    }

    /**
     * 获取文件后缀对应的contentType
     *
     * @author liaoxiting
     * @date 2020/11/2 18:08
     */
    private Map<String, String> getFileContentType() {
        synchronized (LOCK) {
            if (contentType.size() == 0) {
                contentType.put(".bmp", "application/x-bmp");
                contentType.put(".gif", "image/gif");
                contentType.put(".fax", "image/fax");
                contentType.put(".ico", "image/x-icon");
                contentType.put(".jfif", "image/jpeg");
                contentType.put(".jpe", "image/jpeg");
                contentType.put(".jpeg", "image/jpeg");
                contentType.put(".jpg", "image/jpeg");
                contentType.put(".png", "image/png");
                contentType.put(".rp", "image/vnd.rn-realpix");
                contentType.put(".tif", "image/tiff");
                contentType.put(".tiff", "image/tiff");
                contentType.put(".doc", "application/msword");
                contentType.put(".ppt", "application/x-ppt");
                contentType.put(".pdf", "application/pdf");
                contentType.put(".xls", "application/vnd.ms-excel");
                contentType.put(".txt", "text/plain");
                contentType.put(".java", "java/*");
                contentType.put(".html", "text/html");
                contentType.put(".avi", "video/avi");
                contentType.put(".movie", "video/x-sgi-movie");
                contentType.put(".mp4", "video/mpeg4");
                contentType.put(".mp3", "audio/mp3");
            }
        }
        return contentType;
    }

    /**
     * 获取文件后缀对应的contentType
     *
     * @author liaoxiting
     * @date 2020/11/2 18:05
     */
    private String getFileContentType(String fileSuffix) {
        String contentType = getFileContentType().get(fileSuffix);
        if (ObjectUtil.isEmpty(contentType)) {
            return "application/octet-stream";
        } else {
            return contentType;
        }
    }

}

package com.xxw.platform.plugin.file.sdk.aliyun;

import cn.hutool.core.io.IoUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.xxw.platform.plugin.file.api.FileOperatorApi;
import com.xxw.platform.plugin.file.api.enums.BucketAuthEnum;
import com.xxw.platform.plugin.file.api.enums.FileLocationEnum;
import com.xxw.platform.plugin.file.api.exception.FileException;
import com.xxw.platform.plugin.file.api.exception.enums.FileExceptionEnum;
import com.xxw.platform.plugin.file.api.expander.FileConfigExpander;
import com.xxw.platform.plugin.file.api.pojo.props.AliyunOssProperties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云文件操作的实现
 *
 * @author liaoxiting
 * @date 2020/10/26 10:59
 */
public class AliyunFileOperator implements FileOperatorApi {

    /**
     * 阿里云文件操作客户端
     */
    private OSS ossClient;

    /**
     * 阿里云oss的配置
     */
    private final AliyunOssProperties aliyunOssProperties;

    public AliyunFileOperator(AliyunOssProperties aliyunOssProperties) {
        this.aliyunOssProperties = aliyunOssProperties;
        this.initClient();
    }

    @Override
    public void initClient() {
        String endpoint = aliyunOssProperties.getEndPoint();
        String accessKeyId = aliyunOssProperties.getAccessKeyId();
        String accessKeySecret = aliyunOssProperties.getAccessKeySecret();

        // 创建OSSClient实例。
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    public void destroyClient() {
        ossClient.shutdown();
    }

    @Override
    public Object getClient() {
        return ossClient;
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        try {
            return ossClient.doesBucketExist(bucketName);
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void setBucketAcl(String bucketName, BucketAuthEnum bucketAuthEnum) {
        try {
            if (bucketAuthEnum.equals(BucketAuthEnum.PRIVATE)) {
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.Private);
            } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ)) {
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ_WRITE)) {
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicReadWrite);
            }
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean isExistingFile(String bucketName, String key) {
        try {
            return ossClient.doesObjectExist(bucketName, key);
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void storageFile(String bucketName, String key, byte[] bytes) {
        try {
            ossClient.putObject(bucketName, key, new ByteArrayInputStream(bytes));
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void storageFile(String bucketName, String key, InputStream inputStream) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream);
            ossClient.putObject(putObjectRequest);
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public byte[] getFileBytes(String bucketName, String key) {
        InputStream objectContent = null;
        try {
            OSSObject ossObject = ossClient.getObject(bucketName, key);
            objectContent = ossObject.getObjectContent();
            return IoUtil.readBytes(objectContent);
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        } finally {
            IoUtil.close(objectContent);
        }

    }

    @Override
    public void setFileAcl(String bucketName, String key, BucketAuthEnum bucketAuthEnum) {
        try {
            if (bucketAuthEnum.equals(BucketAuthEnum.PRIVATE)) {
                ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.Private);
            } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ)) {
                ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
            } else if (bucketAuthEnum.equals(BucketAuthEnum.PUBLIC_READ_WRITE)) {
                ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.PublicReadWrite);
            }
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void copyFile(String originBucketName, String originFileKey, String newBucketName, String newFileKey) {
        try {
            ossClient.copyObject(originBucketName, originFileKey, newBucketName, newFileKey);
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public String getFileAuthUrl(String bucketName, String key, Long timeoutMillis) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + timeoutMillis);
            URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
            return url.toString();
        } catch (OSSException | ClientException e) {
            // 组装提示信息
            throw new FileException(FileExceptionEnum.ALIYUN_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public String getFileUnAuthUrl(String bucketName, String key) {
        return this.getFileAuthUrl(bucketName, key, FileConfigExpander.getDefaultFileTimeoutSeconds() * 1000);
    }

    @Override
    public void deleteFile(String bucketName, String key) {
        ossClient.deleteObject(bucketName, key);
    }

    @Override
    public FileLocationEnum getFileLocationEnum() {
        return FileLocationEnum.ALIYUN;
    }

}

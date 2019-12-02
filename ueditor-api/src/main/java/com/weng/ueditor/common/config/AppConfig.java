package com.weng.ueditor.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wengzhonghui
 * @date 2019/9/15 8:40
 */
@Component
public class AppConfig {

    // 默认上传文件路径
    public static String DEFAULT_UPLOAD_FILE_PATH = "";
    @Value("${app.file.uploadfile-path}")
    public void getDefaultPassword(final String uploadFilePath){
        DEFAULT_UPLOAD_FILE_PATH = uploadFilePath;
    }

    // 文件上传类型
    public static Integer UPLOAD_TYPE = 1;
    @Value("${app.upload-type}")
    public void getUploadType(final Integer uploadType){
        UPLOAD_TYPE = uploadType;
    }

    // 默认上传文件的访问地址
    public static String DEFAULT_LOCAL_VISIT_URL_PREFIX = "";
    @Value("${app.file.local_file_visit_url_prefix}")
    public void getDefaultLocalFileVisitUrlPrefix(final String urlPrefix){
        DEFAULT_LOCAL_VISIT_URL_PREFIX = urlPrefix;
    }

}

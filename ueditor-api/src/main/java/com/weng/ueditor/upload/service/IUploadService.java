package com.weng.ueditor.upload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author wengzhonghui
 * @date 2019/11/5 15:43
 */
public interface IUploadService {

    /*
     * 上传文件
     * @param [file]
     * @return
     */
    String upload(MultipartFile file);

    /*
     * 上传本地文件
     * @param [tmpFile]
     * @return 上传成功，则返回null ,失败则返回存储路径
     */
    String uploadLocalFile(File tmpFile, String originFileName);
}

package com.weng.ueditor.upload.ceph.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.weng.ueditor.common.utils.ContentTypeOfExtendUtil;
import com.weng.ueditor.upload.service.IUploadService;
import com.weng.ueditor.upload.ceph.utils.CephUtil;
import com.weng.ueditor.upload.ceph.config.CephConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * ceph文件上传
 * @author wengzhonghui
 * @date 2019/11/5 16:00
 */
@Slf4j
@Service("cephUploadService")
public class CephUploadServiceImpl implements IUploadService {
    @Override
    public String upload(MultipartFile file) {

        try {
            long start = System.currentTimeMillis();
            InputStream ins= file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            String contentType = ContentTypeOfExtendUtil.getContentTypeByFileName(file.getOriginalFilename());
            if(contentType!=null){
                metadata.setContentType(contentType);
            }
            metadata.setContentLength(ins.available());
            String key_name = CephUtil.genarateObjKey(file.getOriginalFilename());
            CephUtil.putObject(CephConfig.BUCKET_DEFAULT, key_name, ins, metadata);
            // 过期时间100年后
            String visitUrl = CephUtil.generatePresignedUrl(CephConfig.BUCKET_DEFAULT, key_name, new Date(4730876338000L)).toString();
            log.info("upload File cost:" + (System.currentTimeMillis() - start) + "ms");
            return visitUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadLocalFile(File tmpFile, String originFileName) {
        try {
            long start = System.currentTimeMillis();
            InputStream ins = new FileInputStream(tmpFile);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(ins.available());
            String contentType = ContentTypeOfExtendUtil.getContentTypeByFileName(originFileName);
            if(contentType!=null){
                metadata.setContentType(contentType);
            }
            String key_name = CephUtil.genarateObjKey(originFileName);
            CephUtil.putObject(CephConfig.BUCKET_DEFAULT, key_name, ins, metadata);
            // 过期时间100年后
            String visitUrl = CephUtil.generatePresignedUrl(CephConfig.BUCKET_DEFAULT, key_name, new Date(4730876338000L)).toString();
            log.info("upload File cost:" + (System.currentTimeMillis() - start) + "ms");
            return visitUrl;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
}

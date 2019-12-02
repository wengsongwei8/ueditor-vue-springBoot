package com.weng.ueditor.upload.local.service;

import com.weng.ueditor.upload.service.IUploadService;
import com.weng.ueditor.upload.local.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 本地文件上传
 * @author wengzhonghui
 * @date 2019/11/5 16:00
 */
@Slf4j
@Service("localUploadService")
public class LocalUploadServiceImpl implements IUploadService {
    @Override
    public String upload(MultipartFile file) {

        try {
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID();
            String visitUrl = FileUtils.upload(file,"file/",fileName);
            return visitUrl;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String uploadLocalFile(File file, String originFileName) {

        String fileName = getRandomName();
        try {
            String visitUrl = FileUtils.upload(file,"file/",fileName, originFileName);
            return visitUrl;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        return null;
    }

    private String getRandomName(){
        return System.currentTimeMillis() + "_" + UUID.randomUUID();
    }
}

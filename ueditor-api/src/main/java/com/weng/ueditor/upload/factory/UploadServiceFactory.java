package com.weng.ueditor.upload.factory;

import com.weng.ueditor.common.config.AppConfig;
import com.weng.ueditor.upload.ceph.service.CephUploadServiceImpl;
import com.weng.ueditor.upload.local.service.LocalUploadServiceImpl;
import com.weng.ueditor.upload.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 上传服务工厂类
 * @author wengzhonghui
 * @date 2019/11/6 12:50
 */
@Component
public class UploadServiceFactory {

    @Autowired
    private IUploadService cephUploadService;

    @Autowired
    private IUploadService localUploadService;

    public IUploadService getUploadService(){
        if(AppConfig.UPLOAD_TYPE == 1){
            return new LocalUploadServiceImpl();
        }else if(AppConfig.UPLOAD_TYPE == 2){
            return new CephUploadServiceImpl();
        }
        return localUploadService;
    }
}

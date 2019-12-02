package com.weng.ueditor.common.utils;

import com.weng.ueditor.common.config.AppConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 资源访问路径处理
 * @author wengzhonghui
 * @date 2019/11/19 9:16
 */
public class ResourceUrlUtil {

    public static String convertVisitUrl(String url){
        if(StringUtils.isNotEmpty(url) && url.trim().indexOf("http")!=0){
            url = AppConfig.DEFAULT_LOCAL_VISIT_URL_PREFIX +  "/" + url;
        }
        return url;
    }
}

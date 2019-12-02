package com.weng.ueditor.common.config;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 上传配置
 * @author wengzhonghui
 * @date 2019/11/20 10:43
 */
@Component
public class UploadConfig {


    /**
     * 解决tomcat上传文件大于10M重置导致上传失败的问题
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory tomcatEmbeddedServletContainerFactory(){
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if(connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>){
                ( (AbstractHttp11Protocol<?>)connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });

        return factory;
    }

}

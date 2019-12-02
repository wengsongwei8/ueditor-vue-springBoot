package com.weng.ueditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 启动类
 *
 **/
@SpringBootApplication(scanBasePackages ={
        "com.weng"
})
public class UeditorApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(UeditorApplication.class, args);
	}
}

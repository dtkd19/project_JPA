package com.sh.project_JPA.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvnConfig implements WebMvcConfigurer {
    // upload 경로 제공
    String uploadPath  = "file:///D:\\_myProject\\_java\\_fileUpload\\";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(uploadPath);
    }
}

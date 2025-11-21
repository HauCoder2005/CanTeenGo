package com.CanTinGo.dev.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandler implements WebMvcConfigurer {
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry  registry) {
        registry.addResourceHandler("/images/**")
        		.addResourceLocations("file:C:/CanTinGo/uploads/images/");
    }
}

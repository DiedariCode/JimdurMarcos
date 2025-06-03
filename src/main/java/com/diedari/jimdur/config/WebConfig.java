package com.diedari.jimdur.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esta línea le dice a Spring que cualquier URL /uploads/**
        // será servida desde la carpeta uploads que está fuera de src
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // "file:" indica carpeta en disco
    }
}
package com.diedari.jimdur.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "file.upload")
@Data
public class FileStorageProperties {
    private String uploadDir;
    private String productImagesDir;
} 
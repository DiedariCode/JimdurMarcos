package com.diedari.jimdur.dto;

import org.springframework.web.multipart.MultipartFile;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailFileDTO {
    private String[] toUser;
    private String subject;
    private String message;
    
    private MultipartFile file;
}

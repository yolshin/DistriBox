package com.distribox.aps.dto;

import lombok.Data;

@Data
public class RequestDto {

    private String userId;
    private String filePath;
    private String fileName;
    private String fileContents;

}

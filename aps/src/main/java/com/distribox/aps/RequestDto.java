package com.distribox.aps;

import lombok.Data;

@Data
public class RequestDto {

    private String userId;

    private String filePath;
    private String fileName;
    private String fileContents;

}

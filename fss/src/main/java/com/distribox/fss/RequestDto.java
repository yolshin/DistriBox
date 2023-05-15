package com.distribox.fss;

import lombok.Data;

@Data
public class RequestDto {

    private String userId;

    private String filePath;
    private String fileName;
    private String fileContents;

}

package com.distribox.aps.dto;

import lombok.Data;

import java.util.*;

@Data
public class FileDto {

    private String filePath;
    private Set<String> servers;

}

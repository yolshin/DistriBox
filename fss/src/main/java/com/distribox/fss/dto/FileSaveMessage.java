package com.distribox.fss.dto;

import lombok.Data;

import java.util.*;

@Data
public class FileSaveMessage {
    private String filepath;
    private String userid;
    private List<String> serverids;
}

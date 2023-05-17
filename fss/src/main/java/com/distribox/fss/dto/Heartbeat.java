package com.distribox.fss.dto;

import lombok.Data;

@Data
public class Heartbeat {
    private String server;
    private String time;
    private String status;
}

package com.distribox.fss.services;

import org.springframework.stereotype.Service;

@Service
public class FDSService {

    public String sendAck() {
        // Send ACK through HTTP to FDS.
        return "ACK sent";
    }

}

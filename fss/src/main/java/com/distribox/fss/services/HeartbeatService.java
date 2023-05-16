package com.distribox.fss.services;

import org.springframework.stereotype.Service;

@Service
public class HeartbeatService {

    public void sendHeartbeat() {
        //TODO: make sure that it gets an acknowledgement from the leader
        //and to implement retry logic if it doesn't
        // Send heartbeat.
    }

}

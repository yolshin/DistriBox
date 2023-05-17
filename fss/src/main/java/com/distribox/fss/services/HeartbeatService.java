package com.distribox.fss.services;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HeartbeatService {

    public void sendHeartbeat() {
        //TODO: make sure that it gets an acknowledgement from the leader
        // and to implement retry logic if it doesn't
        // Send heartbeat.
        String ack = "ACK"; // Replace with your ack message
        WebClient.create().post()
                .uri("http://file-distribution-service/ack") // Replace with the appropriate URI
                .contentType(MediaType.APPLICATION_JSON) // Set the content type of the request
                .bodyValue(ack) // Set the ack data in the request body
                .retrieve()
                .bodyToMono(Void.class) // Specify the response type (if needed)
                .block();
    }

}

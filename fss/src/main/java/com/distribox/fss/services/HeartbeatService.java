package com.distribox.fss.services;

import com.distribox.fss.zookeeper.LeaderObserver;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HeartbeatService {

    public void sendHeartbeat() {
        //Note: make sure that it gets an acknowledgement from the leader
        // and to implement retry logic if it doesn't
        // TODO: This code is buggy.
        String ack = "ACK"; // Replace with your ack message
        WebClient client = WebClient.create();
        LeaderObserver leaderObserver = new LeaderObserver();
        String leaderId;
        try {
            leaderId = leaderObserver.getLeaderId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        client.post()
                .uri(leaderId + "/heartbeat") // Replace with the appropriate URI
                .contentType(MediaType.APPLICATION_JSON) // Set the content type of the request
                .bodyValue(ack) // Set the ack data in the request body
                .retrieve()
                .bodyToMono(String.class) // Specify the response type (if needed)
                .retry()
                .block();
    }

}

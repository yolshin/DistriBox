package com.distribox.fss.services;

import com.distribox.fss.zookeeper.LeaderObserver;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class FDSService {

    public void sendAck() {
        // Send ACK through HTTP to FDS.
        //Note: make sure that it gets an acknowledgement from the leader
        // and to implement retry logic if it doesn't
        // TODO: This code is buggy.
        String serverId = "{serverId}"; // TODO: Replace with server id
        String time = "{time}"; // TODO: Replace with time
        WebClient client = WebClient.create();
        LeaderObserver leaderObserver = new LeaderObserver();
        String leaderId;
        try {
            leaderId = leaderObserver.getLeaderId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        client.post()
                .uri(leaderId) // Replace with the appropriate URI
                .contentType(MediaType.APPLICATION_JSON) // Set the content type of the request
                .bodyValue("{Insert json here}") // TODO: Set the ack data in the request body
                .retrieve()
                .bodyToMono(Void.class) // Specify the response type (if needed)
                .retry()
                .block();
    }

}

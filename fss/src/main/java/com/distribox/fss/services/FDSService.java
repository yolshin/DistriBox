package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import com.distribox.fss.zookeeper.LeaderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FDSService {

    @Autowired
    private LeaderObserver leaderObserver;

    public void sendAck(RequestDto file) {
        // Send ACK through HTTP to FDS.
        String leaderUrl = leaderObserver.getLeaderId();
        String filePath = file.getUserId() + "/" + file.getFilePath() + "/" + file.getFileName();
//        String uri = leaderUrl + "/savedFile?filePath=" + filePath.replaceAll("/", "%2F");

        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/savedFile")
                .queryParam("filePath", filePath)
                .encode()
                .toUriString();

        WebClient webClient = WebClient.create();

        String responseEntity = webClient.post()

            .uri(leaderUrl + "/savedFile")
            .bodyValue(filePath)
    //                .contentType(MediaType.TEXT_PLAIN)
    //                .body(BodyInserters.fromValue(filePath))
            .retrieve()
            .bodyToMono(String.class)
            .block();

    }

}

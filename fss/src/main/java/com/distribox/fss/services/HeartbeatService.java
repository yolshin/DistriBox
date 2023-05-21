package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import com.distribox.fss.dto.FileSaveMessage;
import com.distribox.fss.dto.Heartbeat;
import com.distribox.fss.zookeeper.LeaderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
@EnableScheduling
public class HeartbeatService {

    @Autowired
    private LeaderObserver leaderObserver;

    @Value("${server.url}")
    private String serverUrl;

    private boolean serverIsBusy = false;

    public void setAsBusy() {
        serverIsBusy = true;
        sendHeartbeat();
    }
    public void setAsOpen() {
        serverIsBusy = false;
        sendHeartbeat();
    }

    @Scheduled(fixedRate = 30000) // Run every 30 seconds
    public void sendHeartbeat() {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setServer(serverUrl);
        heartbeat.setTime(String.valueOf(System.currentTimeMillis()));
        if (serverIsBusy) {
            heartbeat.setServer("BUSY");
        }
        else {
            heartbeat.setStatus("OPEN");
        }

        String url = leaderObserver.getLeaderId() + "/heartbeat";

        WebClient client = WebClient.create();

        client.post()
                .uri(url)  // Replace with your endpoint URL
                .body(Mono.just(heartbeat), Heartbeat.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void updateFDS(RequestDto file) {
        // Send ACK through HTTP to FDS.
        String leaderUrl = leaderObserver.getLeaderId();
        String filePath = file.getUserId() + "/" + file.getFilePath() + "/" + file.getFileName();

        FileSaveMessage fileSaveMessage = new FileSaveMessage();
        fileSaveMessage.setFilepath(filePath);
        fileSaveMessage.setServerids(List.of(serverUrl));
        fileSaveMessage.setUserid(file.getUserId());

        String url = leaderUrl + "/saveFile";

        WebClient client = WebClient.create();

        // TODO: Is this appropriate use of retry? Perhaps we should get rid
        //  of block() since it waits indefinitely. Might defeat the entire purpose
        //  of retry.
        client.post()
                .uri(url)  // Replace with your endpoint URL
                .body(Mono.just(fileSaveMessage), FileSaveMessage.class)
                .retrieve()
                .bodyToMono(Void.class)
//                .retryWhen(Retry.backoff(3, Duration.ofSeconds(30)))
                .block();
    }
}

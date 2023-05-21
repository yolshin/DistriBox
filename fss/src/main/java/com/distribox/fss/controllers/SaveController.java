package com.distribox.fss.controllers;

import com.distribox.fss.RequestDto;
import com.distribox.fss.services.FDSService;
import com.distribox.fss.services.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@RestController
public class SaveController {

    @Autowired
    SaveService saveService; // automatically-instantiated singleton

    @Autowired
    FDSService fdsService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody RequestDto file) {
        // Save file to disk. (call SaveService)
        try {
            saveService.saveFile(file);
        } catch (IllegalArgumentException | FileAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException ioException) {
            return ResponseEntity.internalServerError().body(ioException.getMessage());
        }
        // Send ACK to FDS. (fds stores metadata of file - metadata includes file name and where it is stored as well
        //  as server status)
//        fdsService.sendAck(file); // TODO: Redo Sending ACK to APS!
        //TODO: Return response body!
        return ResponseEntity.ok("File Saved!");
    }

    @PostMapping("/test")
    public String test(@RequestBody String url) {
        WebClient client = WebClient.create();

        client.post()
                .uri(url)  // Replace with your endpoint URL
                .bodyValue("Hello World")
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return "File Saved!";
    }

    @PostMapping("/test1")
    public String test1(@RequestBody String hey) {
        return hey;
    }

}

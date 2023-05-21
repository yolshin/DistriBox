package com.distribox.fss.controllers;

import com.distribox.fss.RequestDto;
import com.distribox.fss.services.RetrieveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
public class GetController {

    @Autowired
    RetrieveService retrieveService;

    // Note: We don't use GetMapping here since that involves RequestParams, whereas we are using RequestBody.
    @PostMapping("/get")
    @ResponseBody
    public ResponseEntity<String> get(@RequestBody RequestDto file) {
        String fileContents;
        // Retrieve file.
        try {
            fileContents = retrieveService.getFile(file);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        // TODO: Return fileContents in ResponseBody.
        return ResponseEntity.ok(fileContents);
    }

}

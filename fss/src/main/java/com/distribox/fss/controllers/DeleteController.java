package com.distribox.fss.controllers;

import com.distribox.fss.RequestDto;
import com.distribox.fss.services.DeleteService;
import com.distribox.fss.services.FDSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
public class DeleteController {

    @Autowired
    DeleteService deleteService;

    @Autowired
    FDSService fdsService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody RequestDto file) {
        // Delete file.
        try {
            deleteService.deleteFile(file);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        // Send ACK to FDS (?).
        fdsService.sendAck(file);
        // TODO: Return response body!
        return ResponseEntity.ok().body("File deleted!");
    }

}

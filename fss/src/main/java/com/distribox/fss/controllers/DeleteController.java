package com.distribox.fss.controllers;

import com.distribox.fss.services.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteController {

    @Autowired
    DeleteService deleteService;

    @DeleteMapping("/delete")
    public void delete(@RequestBody String file) {
        // Delete file.
        deleteService.deleteFile(file);
        // Send ACK to FDS (?).
    }

}

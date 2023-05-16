package com.distribox.fss.controllers;

import com.distribox.fss.RequestDto;
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
    public String delete(@RequestBody RequestDto file) {
        // Delete file.
        return deleteService.deleteFile(file);
        // Send ACK to FDS (?).
        // TODO: Return response body!
    }

}

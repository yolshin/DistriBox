package com.distribox.fss.controllers;

import com.distribox.fss.services.RetrieveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetController {

    @Autowired
    RetrieveService retrieveService;

    @GetMapping("/get")
    public String get(@RequestBody String file) {
        // Retrieve file.
        String fileContents = retrieveService.getFile(file);
        // TODO: Return fileContents in ResponseBody.
        return fileContents;
    }

}

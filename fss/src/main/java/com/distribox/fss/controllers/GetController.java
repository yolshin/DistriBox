package com.distribox.fss.controllers;

import com.distribox.fss.RequestDto;
import com.distribox.fss.services.RetrieveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetController {

    @Autowired
    RetrieveService retrieveService;

    @PostMapping ("/get")
    public String get(@RequestBody RequestDto file) {
        // Retrieve file.
        return retrieveService.getFile(file);
        // TODO: Return fileContents in ResponseBody.
    }

}

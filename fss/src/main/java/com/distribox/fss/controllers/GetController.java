package com.distribox.fss.controllers;

import com.distribox.fss.RequestDto;
import com.distribox.fss.services.RetrieveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class GetController {

    @Autowired
    RetrieveService retrieveService;

    // Note: We don't use GetMapping here since that involves RequestParams, whereas we are using RequestBody.
    @PostMapping("/get")
    @ResponseBody
    public Object get(@RequestBody RequestDto file) {
        // Retrieve file.
        try {
            return retrieveService.getFile(file);
        } catch (RuntimeException e) {
            throw new RuntimeException("Internal server error", e);
        }
        // TODO: Return fileContents in ResponseBody.
    }

}

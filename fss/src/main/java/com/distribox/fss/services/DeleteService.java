package com.distribox.fss.services;

import org.springframework.stereotype.Service;

@Service
public class DeleteService {

    public String deleteFile(String file) {
        // Delete file.
        return "File deleted";
    }

}

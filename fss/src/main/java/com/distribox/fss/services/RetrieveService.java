package com.distribox.fss.services;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class RetrieveService {

    public void getFile(String file) {
        // Get file.
        String fileName = ""; // TODO: Fill this in.
        File fileOnDisk = new File("data" + File.separator + fileName);
        //TODO: Finish this.
    }

}

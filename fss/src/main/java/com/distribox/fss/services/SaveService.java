package com.distribox.fss.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SaveService {

    public void saveFile(String file) throws IOException {
        // Parse file.
        // Save to disk.
        String fileName = ""; // TODO: Fill this in!
        File fileOnDisk = new File("data" + File.separator + fileName);
        fileOnDisk.mkdirs();
        fileOnDisk.createNewFile();
    }

}

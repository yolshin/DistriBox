package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DeleteService {

    public String deleteFile(RequestDto file) {
        // Delete file.
        String username = file.getUserId(); // Will be needed since empty directories cannot be deleted at this point. // TODO: Fill the empty string in with username!
        String path = username + File.separator + file.getFilePath() + File.separator + file.getFileName(); // includes username (first part) // TODO: Fill this in!
        File filePath = new File("data" + File.separator + path);
        if (filePath.exists()) {
            return String.valueOf(filePath.delete());
        }
        return null;
    }

}

package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class DeleteService {

    public void deleteFile(RequestDto file) throws IOException {
        // Delete file.
        String username = file.getUserId(); // Will be needed since empty directories cannot be deleted at this point.
        String path = username + File.separator + file.getFilePath() + File.separator + file.getFileName(); // includes username (first part)
        File filePath = new File("data" + File.separator + path);
        if (!filePath.exists()) {
            throw new FileNotFoundException();
        } else if (filePath.isDirectory()) {
            throw new IllegalArgumentException("Cannot delete directory!");
        }
        boolean deleteSuccessful = filePath.delete();
        if (!deleteSuccessful) {
            throw new IOException("Delete was not successful!");
        }
    }

}

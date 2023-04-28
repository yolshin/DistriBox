package com.distribox.fss.services;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class SaveService {

    public void saveFile(String file) {
        // Parse file.

        String username = ""; // TODO: Fill this in!
        String filePath = ""; // includes username (first part) // TODO: Fill this in!
        String fileName = ""; // Name of file without path. // TODO: Fill this in!
        String fileContents = ""; // TODO: Fill this in!

        if (fileName.equals(username) || fileName.equals("data")) {
            throw new RuntimeException("Illegal file name: " + fileName);
        }

        // Save to disk.
        File dirPath = new File("data" + File.separator + filePath);
        dirPath.mkdirs();
        File fileWithPath = new File(dirPath.getPath() + File.separator + fileName);
        try {
            fileWithPath.createNewFile();
        } catch (IOException ignored) {
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileWithPath, true))) {
            fileWriter.write(fileContents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

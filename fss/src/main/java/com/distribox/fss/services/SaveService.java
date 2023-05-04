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

        String username = "user"; // TODO: Fill this in!
        String filePath = "user" + File.separator + "folder"; // includes username (first part) // TODO: Fill this in!
        String fileName = "file"; // Name of file without path. // TODO: Fill this in!
        String fileContents = "file contents"; // TODO: Fill this in!

        System.out.println(fileName);
        System.out.println(username);
        if (fileName.equals(username) || fileName.equals("data")) {
            throw new RuntimeException("Illegal file name: " + fileName);
        }

        String dirPath = System.getProperty("user.dir");

        // Save to disk.
        File dirPath1 = new File(dirPath + File.separator + "data" + File.separator + filePath);
        dirPath1.mkdirs();
        File fileWithPath = new File(dirPath1.getPath() + File.separator + fileName);
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

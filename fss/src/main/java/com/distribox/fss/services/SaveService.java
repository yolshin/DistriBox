package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class SaveService {

    public void saveFile(RequestDto file) {
        // Parse file.

        String username = file.getUserId(); // TODO: Fill this in!
        String filePath = username + File.separator + file.getFilePath(); // includes username (first part) // TODO: Fill this in!
        String fileName = file.getFileName(); // Name of file without path. // TODO: Fill this in!
        String fileContents = file.getFileContents(); // TODO: Fill this in!

        System.out.println(fileName);
        System.out.println(username);
        if (fileName.equals(username) || fileName.equals("data")) {
            throw new RuntimeException("Illegal file name: " + fileName);
        }

        String dirPath = System.getProperty("user.dir");

        // Save to disk.
        File dirPath1 = new File(dirPath + File.separator + "data" + File.separator + filePath);
        boolean dirsMade = dirPath1.mkdirs();
        File fileWithPath = new File(dirPath1.getPath() + File.separator + fileName);
        try {
            boolean fileMade = fileWithPath.createNewFile();
        } catch (IOException ignored) {
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileWithPath, true))) {
            fileWriter.write(fileContents);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

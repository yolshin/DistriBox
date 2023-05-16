package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class SaveService {

    @Value("${fss.data.dir}")
    private String dataDir;

    public void saveFile(RequestDto file) {
        // Parse file.

        String username = file.getUserId(); // TODO: Fill this in!
        String filePath = dataDir + File.separator + username + File.separator + file.getFilePath(); // includes username (first part) // TODO: Fill this in!
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
        System.out.println(dirPath1.getPath());
        boolean dirsMade = dirPath1.mkdirs();
        System.out.println("Dirs made: " + dirsMade);
        File fileWithPath = new File(dirPath1.getPath() + File.separator + fileName);
        try {
            boolean fileMade = fileWithPath.createNewFile();
            System.out.println("File made: " + fileMade);
        } catch (IOException ignored) {
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileWithPath, true))) {
            fileWriter.write(fileContents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

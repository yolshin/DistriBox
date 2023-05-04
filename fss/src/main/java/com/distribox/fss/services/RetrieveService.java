package com.distribox.fss.services;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class RetrieveService {

    public String getFile(String file) {
        // Get file.
        String fileName = "user" + File.separator + "folder" + File.separator + "file"; // This includes the full path. // TODO: Fill this in.
        File fileOnDisk = new File("data" + File.separator + fileName);
        StringBuilder fileContents = new StringBuilder();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileOnDisk))) {
            boolean appendLineSeparator = false;
            String line;
            while ((line = fileReader.readLine()) != null) {
                // This will only append a line separator once we have already
                // begun reading the file. That way, there will also be no extra
                // line separator added at the end of the file.
                if (!appendLineSeparator) {
                    appendLineSeparator = true;
                } else {
                    fileContents.append(System.lineSeparator());
                }
                fileContents.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(fileContents);
    }

}

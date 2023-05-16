package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class RetrieveService {

    @Value("${fss.data.dir}")
    private String dataDir;

    public String getFile(RequestDto file) {
        // Get file.
        String fileName = dataDir + File.separator + file.getUserId() + File.separator + file.getFilePath() + File.separator + file.getFileName(); // This includes the full path. // TODO: Fill this in.
        File fileOnDisk = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + fileName);
        if (!fileOnDisk.canRead()) {
            return null;
        }
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

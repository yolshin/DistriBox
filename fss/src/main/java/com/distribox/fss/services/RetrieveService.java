package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class RetrieveService {

    @Value("${fss.data.dir}")
    private String dataDir;

    public String getFile(RequestDto file) throws IOException {
        // Get file.
        String fileName = dataDir + File.separator + file.getUserId() + File.separator + file.getFilePath() + File.separator + file.getFileName(); // This includes the full path. // TODO: Fill this in.
        File fileOnDisk = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + fileName);
        if (!fileOnDisk.exists()) {
            throw new FileNotFoundException();
        }
        else if (fileOnDisk.isDirectory()) {
            throw new IllegalArgumentException(fileOnDisk.getPath() + " is a directory!");
        }
        else if (!fileOnDisk.canRead()) {
            throw new IllegalArgumentException("File cannot be read: " + fileOnDisk.getPath());
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
            throw new IOException("There was a problem reading the file for: " + fileOnDisk.getPath());
        }
        return new String(fileContents);
    }

}

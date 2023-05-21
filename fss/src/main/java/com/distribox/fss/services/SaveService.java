package com.distribox.fss.services;

import com.distribox.fss.RequestDto;
import com.distribox.fss.dto.Heartbeat;
import com.distribox.fss.zookeeper.LeaderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@Service
public class SaveService {

    @Autowired
    private HeartbeatService heartbeatService;

    @Value("${fss.data.dir}")
    private String dataDir; // which file server service the data is being saved on

    public void saveFile(RequestDto file) throws IOException {
        // Parse file.

        String username = file.getUserId();
        String filePath = dataDir + File.separator + username + File.separator + file.getFilePath(); // includes username (first part)
        String fileName = file.getFileName(); // Name of file without path.
        String fileContents = file.getFileContents();

        System.out.println(fileName);
        System.out.println(username);
        if (fileName.equals(username) || fileName.equals(dataDir) || fileName.equals("data")) {
            throw new IllegalArgumentException("Illegal file name: " + fileName);
        }

        String dirPath = System.getProperty("user.dir");

        // Save to disk.
        File dirPath1 = new File(dirPath + File.separator + "data" + File.separator + filePath);
        System.out.println(dirPath1.getPath());
        boolean dirsMade = dirPath1.mkdirs();
        System.out.println("Dirs made: " + dirsMade);
        File fileWithPath = new File(dirPath1.getPath() + File.separator + fileName);
        if (fileWithPath.exists()) {
            throw new FileAlreadyExistsException("File already exists: " + fileWithPath.getPath());
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileWithPath, true))) {
            fileWithPath.createNewFile();
            System.out.println("File made: " + fileWithPath.getPath());
            fileWriter.write(fileContents);
        } catch (IOException e) {
            throw new IOException(e);
        }

        heartbeatService.updateFDS(file);
    }

}

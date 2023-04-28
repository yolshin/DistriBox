package com.distribox.fss.components;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FolderInitializerComponent {

    @PostConstruct
    public void initializeFolder() {
        String path = "data";
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
    }

}

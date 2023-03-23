package capstone.distribox.project.controllers;

import capstone.distribox.project.dtos.File;
import capstone.distribox.project.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/files")
    public List<File> getAllFiles() {
        return fileService.getAllFiles();
    }

}

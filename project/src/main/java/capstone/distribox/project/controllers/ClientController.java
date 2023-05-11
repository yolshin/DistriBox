package capstone.distribox.project.controllers;

import capstone.distribox.project.entities.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController {


    @GetMapping("/files")
    public List<File> getAllFiles() {
        return null;
    }

    @GetMapping("/get")
    public List<File> getFile(@RequestBody String filename) {
        System.out.println("This is the file name: " + filename);
        return null;
    }

}

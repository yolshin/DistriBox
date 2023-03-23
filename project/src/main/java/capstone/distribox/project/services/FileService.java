package capstone.distribox.project.services;

import capstone.distribox.project.dtos.File;
import capstone.distribox.project.repos.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepo fileRepo;

    public List<File> getAllFiles() {
        return fileRepo.findAll();
    }

}

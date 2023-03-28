package capstone.distribox.project.services;

import capstone.distribox.project.entities.File;
import capstone.distribox.project.repos.FilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

	@Autowired
	FilesRepository filesRepository;
	private static final Logger log = LoggerFactory.getLogger(FileService.class);

	public void getFiles() {
		List<File> files = new ArrayList<>(filesRepository.findAll());
		for (File f : files) {
			log.info("File " + f.fileid);
		}
	}

	public void addFile() {
		File newFile = new File("createdFile.txt", 0, "mberk");
		filesRepository.save(newFile);
	}

}

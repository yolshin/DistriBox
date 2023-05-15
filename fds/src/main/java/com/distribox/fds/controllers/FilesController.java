package com.distribox.fds.controllers;

import com.distribox.fds.entities.File;
import com.distribox.fds.entities.Server;
import com.distribox.fds.entities.User;
import com.distribox.fds.repos.FilesRepository;
import com.distribox.fds.repos.ServersRepository;
import com.distribox.fds.repos.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FilesController {
	@Autowired
	ServersRepository serversRepository;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	FilesRepository filesRepository;

	Map<String, File> fileMap = new HashMap<>();

	private static final Logger log = LoggerFactory.getLogger(FilesController.class);

	@PostMapping("/saveFile")
	public File saveFilesRequest(@RequestBody Map<String, Object> body) {
		//TODO: How to handle nonexistant user?
		log.info("Save!");
		String filepath = (String) body.get("filepath");
		String userid = (String) body.get("userid");
		User user;
		if (!usersRepository.existsByUseridLike(userid)) {
			user = new User(userid);
			usersRepository.save(user);
		} else {
			user = usersRepository.findByUserid(userid);
		}
		List<String> serverids = (List<String>) body.get("serverids");
		Set<Server> serverSet = new HashSet<>();
		for (String serverid : serverids) {
			Server s = serversRepository.findById(UUID.fromString(serverid)).get();
			serverSet.add(s);
		}
		File newFile = new File(filepath, user);
		newFile.addServers(serverSet);
		fileMap.put(newFile.getFileid().toString(), newFile);

		return newFile;
	}

	@GetMapping("/getFile")
	public ResponseEntity<File> getFilesRequest(String fileid) {
		UUID fileUUID = UUID.fromString(fileid);
		Optional<File> fileOp = filesRepository.findById(fileUUID);
		if (fileOp.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		} else {
			File file = fileOp.get();
			return ResponseEntity.ok(file);
		}
	}

	@PostMapping("/deleteFile")
	public File deleteFilesReuqest(String fileid) {
		UUID fileUUID = UUID.fromString(fileid);
		File file = filesRepository.getReferenceById(fileUUID);
		filesRepository.delete(file);
		//TODO: Test that it's also gone from owning servers
		return file;
	}

	///Endpoint for ack
	@PostMapping("/savedFile")
	public ResponseEntity<String> saveAck(String fileid) {
		//todo: make some kind of queue for files waiting to be saved
		File fileToSave = fileMap.get(fileid);
		if (fileToSave == null) {
			return ResponseEntity.badRequest().body("Invalid fileid");
		}
		fileToSave = filesRepository.save(fileToSave);
		Set<Server> serverSet = fileToSave.getServers();
		serverSet = new HashSet<>(serversRepository.saveAll(serverSet));
		fileMap.remove(fileid);

		return ResponseEntity.ok("OK");
	}

	//TODO: Send DB updates to other DBs
}

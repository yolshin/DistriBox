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

	private static final Logger log = LoggerFactory.getLogger(FilesController.class);

	@PostMapping("/saveFile")
	public ResponseEntity<String> saveFilesRequest(@RequestBody Map<String, Object> body) {
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
		newFile = filesRepository.save(newFile);
		newFile.addServers(serverSet);
		newFile = filesRepository.save(newFile);
		serverSet = new HashSet<>(serversRepository.saveAll(serverSet));
		return ResponseEntity.ok("file saved");
	}

}

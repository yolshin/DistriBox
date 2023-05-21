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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
	public ResponseEntity<File> saveFilesRequest(@RequestBody Map<String, Object> body) {
		log.info("Save!");
		boolean invalid = false;
		String filepath = (String) body.get("filepath");
		String userid = (String) body.get("userid");
		List<String> serverids = (List<String>) body.get("serverids");
		if (filepath == null || filepath.equals("")) {
			invalid = true;
		}
		if (userid == null || userid.equals("")) {
			invalid = true;
		}
		if (serverids == null || serverids.size() == 0) {
			invalid = true;
		}
		if (invalid) {
			return ResponseEntity.badRequest().build();
		}
		User user;
		Optional<File> existingFile = filesRepository.findByFilepath(filepath);
		if (fileMap.containsKey(filepath)) {
			return mergeServers(fileMap.get(filepath), serverids);
		}
		if (existingFile.isPresent()) {
			return mergeServers(existingFile.get(), serverids);
		}
		if (!usersRepository.existsByUseridLike(userid)) {
			user = new User(userid);
			usersRepository.save(user);
		} else {
			user = usersRepository.findByUserid(userid);
		}
		Set<Server> serverSet = new HashSet<>();
		for (String serverid : serverids) {
			Server s = serversRepository.findById(serverid).get();
			serverSet.add(s);
		}
		File newFile = new File(filepath, user);
		newFile.addServers(serverSet);
		fileMap.put(newFile.getFilepath(), newFile);
//		ResponseEntity<File> response = saveFileToDB(newFile);
		return ResponseEntity.ok(newFile);
	}

	public ResponseEntity<File> mergeServers(File existingFile, List<String>newIds) {
		Set<Server>incomingServerSet = newIds.stream()
				.map(id -> serversRepository.findById(id).get()).collect(Collectors.toSet());
		existingFile.addServers(incomingServerSet);
		fileMap.put(existingFile.getFilepath(), existingFile);
		return ResponseEntity.ok(existingFile);
	}


	@GetMapping("/getFile")
	public ResponseEntity<File> getFileById(String filePath) {
		Optional<File> fileOp = filesRepository.findByFilepath(filePath);
		if (fileOp.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		} else {
			File file = fileOp.get();
			return ResponseEntity.ok(file);
		}
	}

	@PostMapping("/deleteFile")
	public File deleteFilesReuqest(String filepath) {
		File file = filesRepository.findByFilepath(filepath).get();
		filesRepository.delete(file);
		fileMap.remove(filepath);
		//TODO: Test that it's also gone from owning servers
		return file;
	}

	/**
	 * Endpoint for ACK
	 * @param filePath ID of the file sending an ACK for
	 * @return An OK response if the file exists and is saved, badRequest if not.
	 */
	@PostMapping("/savedFile")
	public ResponseEntity<String> saveAck(@RequestBody String filePath) {
		String decodedPath = UriUtils.decode(filePath, StandardCharsets.UTF_8)
				.replaceAll("filePath=", "");
		File fileToSave = fileMap.get(decodedPath);
		if (fileToSave == null) {
				return ResponseEntity.badRequest().body("Invalid filepath");
		}
		saveFileToDB(fileToSave);
		return ResponseEntity.ok("OK");
	}

	private ResponseEntity<File> saveFileToDB(File fileToSave) {
		fileToSave = filesRepository.save(fileToSave);
		Set<Server> serverSet = fileToSave.getServers();
		serverSet = new HashSet<>(serversRepository.saveAll(serverSet));
		for (Server server : serverSet) {
			server.addFile(fileToSave);
			fileToSave.addServer(server);
			serversRepository.save(server);
		}
		filesRepository.save(fileToSave);
		fileMap.remove(fileToSave.getFilepath());
		return ResponseEntity.ok(fileToSave);
	}

	//TODO: Send DB updates to other DBs

	@DeleteMapping("/removeServerFromFile")
	public ResponseEntity<String> removeServerFromFile(@RequestBody Map<String, String>removeInfo) {
		String filePath = removeInfo.get("filePath");
		String serverid = removeInfo.get("serverid");
		Optional<File> fileOpt = filesRepository.findByFilepath(filePath);
		if (fileOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		File file = fileOpt.get();
		Optional<Server> serverOpt = serversRepository.findById(serverid);
		if (serverOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Server server = serverOpt.get();
		file.removeServer(server);
		file = filesRepository.save(file);
		server = serversRepository.save(server);
		return ResponseEntity.ok("Removed " + serverid + " from " + filePath);
	}

}

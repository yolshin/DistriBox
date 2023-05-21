package com.distribox.fds.controllers;

import com.distribox.fds.entities.File;
import com.distribox.fds.entities.Server;
import com.distribox.fds.entities.User;
import com.distribox.fds.repos.FilesRepository;
import com.distribox.fds.repos.ServersRepository;
import com.distribox.fds.repos.UsersRepository;
import com.distribox.fds.zookeeper.ZookeeperConfig;
import org.apache.coyote.Request;
import org.apache.curator.framework.recipes.leader.Participant;
import io.swagger.v3.oas.annotations.servers.Servers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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

	@Autowired
	ZookeeperConfig zookeeperConfig;

//	@Autowired
//	RestTemplate restTemplate;

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
		if (!usersRepository.existsByUseridLike(userid)) {
			user = new User(userid);
			usersRepository.save(user);
		} else {
			user = usersRepository.findByUserid(userid);
		}
		Optional<File> existingFile = filesRepository.findById(filepath);
		if (fileMap.containsKey(filepath)) {
			return mergeServers(fileMap.get(filepath), serverids, body);
		}
		if (existingFile.isPresent()) {
			return mergeServers(existingFile.get(), serverids, body);
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
		System.out.println("hey!!!!");
		resendRequest(HttpMethod.POST, "/saveFile", body);
		return ResponseEntity.ok(newFile);
	}

	public ResponseEntity<File> mergeServers(File existingFile, List<String>newIds, Map<String, Object> body) {
		Set<Server>incomingServerSet = newIds.stream()
				.map(id -> serversRepository.findById(id).get()).collect(Collectors.toSet());
		existingFile.addServers(incomingServerSet);
		fileMap.put(existingFile.getFilepath(), existingFile);
		resendRequest(HttpMethod.POST, "/saveFile", body);
		return ResponseEntity.ok(existingFile);
	}


	@PutMapping("/getFile")
	public ResponseEntity<File> getFileById(@RequestBody String filePath) {
		Optional<File> fileOp = filesRepository.findByFilepath(filePath);
		if (fileOp.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			File file = fileOp.get();
			return ResponseEntity.ok(file);
		}
	}

	@PutMapping("/getFileList")
	public ResponseEntity<List<String>> getFileServerListById(@RequestBody String filePath) {
		Optional<File> fileOp = filesRepository.findByFilepath(filePath);
		if (fileOp.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		} else {
			File file = fileOp.get();
			return ResponseEntity.ok(
					file.getServers()
					.stream()
					.map(Server::getId)
					.collect(Collectors.toList()));
		}
	}

	@PostMapping("/deleteFile")
	public ResponseEntity<File> deleteFilesReuqest(@RequestBody String filepath) {
		String decodedPath = UriUtils.decode(filepath, StandardCharsets.UTF_8)
				.replaceAll("filepath=", "");
		System.out.println("filePath to delete: " + decodedPath);
		Optional<File> fileOpt = filesRepository.findById(decodedPath);
		if (fileOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		File file = fileOpt.get();
		for (Server server: file.getServers()) {
			file.removeServer(server);
			serversRepository.save(server);
		}
		filesRepository.delete(file);
		fileMap.remove(decodedPath);
		//TODO: Test that it's also gone from owning servers
		resendRequest(HttpMethod.POST, "/deleteFile", filepath);
		return ResponseEntity.ok(file);
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
		resendRequest(HttpMethod.POST, "/savedFile", filePath);
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
		resendRequest(HttpMethod.DELETE, "/removeServerFromFile", removeInfo);
		return ResponseEntity.ok("Removed " + serverid + " from " + filePath);
	}

	public void resendRequest(HttpMethod method, String urlPath, Object body) {
		if (!zookeeperConfig.isLeader()) {
			return;
		}
		List<Participant> participants = zookeeperConfig.getParticipants();
		for (Participant participant: participants) {
			String baseURL = participant.getId();
			if (baseURL.equals(zookeeperConfig.leaderId())) {
				continue;
			}
			String url = baseURL + urlPath;
			System.out.println(baseURL);
			RequestEntity<Object> request = RequestEntity.method(method, url).body(body);
			log.info("Sending request " + request.toString() + " to server " + url);
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> response = template.exchange(request, String.class);

		}
	}

}

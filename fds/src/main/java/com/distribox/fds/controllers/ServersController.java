package com.distribox.fds.controllers;

import com.distribox.fds.entities.*;
import com.distribox.fds.repos.FilesRepository;
import com.distribox.fds.repos.ServersRepository;
import com.distribox.fds.repos.UsersRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ServersController {

	@Autowired
	ServersRepository serversRepository;

	@Autowired
	UsersRepository usersRepository;

	private static final Logger log = LoggerFactory.getLogger(ServersController.class);
	@Autowired
	private FilesRepository filesRepository;

	public List<Server> getServers(String fileid, Server.State state) {
		Sort sort = Sort.by("lastSeen").descending();
		if (fileid == null) {
			if (state == null) {
				return serversRepository.findAll(sort);
			}
			return serversRepository.findByState(state, sort);
		}
		if (state == null) {
			return serversRepository.findByFiles_fileid(UUID.fromString(fileid), sort);
		}
		return serversRepository.findByStateAndFiles_fileid(state, UUID.fromString(fileid), sort);
	}

	@GetMapping("/servers")
	@ResponseBody
	public ResponseEntity<List<Server>> getServersRequest(@RequestParam(required = false) String fileid,
	                                      @RequestParam(required = false) Server.State state) {
		List<Server> servers = getServers(fileid, state);
		ResponseEntity<List<Server>> response = ResponseEntity.ok(servers);
		log.info("HEY!!!!!");
		return response;
	}

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

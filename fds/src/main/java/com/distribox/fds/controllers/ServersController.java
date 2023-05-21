package com.distribox.fds.controllers;

import com.distribox.fds.entities.*;
import com.distribox.fds.repos.FilesRepository;
import com.distribox.fds.repos.ServersRepository;
import com.distribox.fds.repos.UsersRepository;
import com.distribox.fds.zookeeper.ZookeeperConfig;
import jakarta.transaction.Transactional;
import org.apache.coyote.Request;
import org.apache.curator.framework.recipes.leader.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ServersController {

	@Autowired
	ServersRepository serversRepository;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	ZookeeperConfig zookeeperConfig;


	private static final Logger log = LoggerFactory.getLogger(ServersController.class);
	@Autowired
	private FilesRepository filesRepository;

	public List<Server> getServers(String filePath, Server.State state) {
		Sort sort = Sort.by("lastSeen").descending();
		if (filePath == null) {
			if (state == null) {
				return serversRepository.findAll(sort);
			}
			return serversRepository.findByState(state, sort);
		}
		if (state == null) {
			return serversRepository.findByFiles_filepath(filePath, sort);
		}
		return serversRepository.findByStateAndFiles_filepath(state, filePath, sort);
	}

	@GetMapping("/servers")
	@ResponseBody
	public ResponseEntity<List<Server>> getServersRequest(@RequestParam(required = false) String filePath,
	                                      @RequestParam(required = false) Server.State state) {
		List<Server> servers = getServers(filePath, state);
		ResponseEntity<List<Server>> response = ResponseEntity.ok(servers);
		return response;
	}

	@GetMapping("/serverids")
	@ResponseBody
	public ResponseEntity<List<String>> getServerIds(@RequestParam(required = false) String filePath,
	                                                      @RequestParam(required = false) Server.State state) {
		List<String> servers = getServers(filePath, state).stream().map(s -> s.getId()).toList();
		ResponseEntity<List<String>> response = ResponseEntity.ok(servers);
		return response;
	}

	@PostMapping("/heartbeat")
	public ResponseEntity<String> postHeartbeat(@RequestBody Map<String, String> body) {
		//TODO: Add test for heartbeat
		String serverId = body.get("server");
		String serverTime = body.get("time");
		Long lastUsedTime = Long.parseLong(serverTime);
		System.out.println("Heartbeat from " + serverId + " at " + lastUsedTime + " received");
		Optional<Server> serverOpt;
//		if (serversRepository.count() > 0) {
			serverOpt = serversRepository.findById(serverId);
//		} else {
//			serverOpt = Optional.of(null);
//		}
		Server server;
		server = serverOpt.orElseGet(() -> new Server(serverId));
		server.setLastSeen(lastUsedTime);
		serversRepository.save(server);
		resendRequest(HttpMethod.POST, "/heartbeat", body);
		return ResponseEntity.ok("OK");
	}

	public void resendRequest(HttpMethod method, String urlPath, Object body) {
		if (!zookeeperConfig.isLeader()) {
			return;
		}
		List<Participant> participants = zookeeperConfig.getParticipants();
		for (Participant participant : participants) {
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

package com.distribox.fds.controllers;

import com.distribox.fds.entities.*;
import com.distribox.fds.repos.ServersRepository;
import org.apache.coyote.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ServersController {

	@Autowired
	ServersRepository serversRepository;

	private static final Logger log = LoggerFactory.getLogger(ServersController.class);

	public List<Server> getServers(String fileid, Server.State state) {
		if (fileid == null) {
			if (state == null) {
				return serversRepository.findAll();
			}
			return serversRepository.findByState(state);
		}
		if (state == null) {
			return serversRepository.findByFiles_fileid(UUID.fromString(fileid));
		}
		return serversRepository.findByStateAndFiles_fileid(state, UUID.fromString(fileid));
	}

	@GetMapping("/servers")
	@ResponseBody
	public List<Server> getServersRequest(@RequestParam(required = false) String fileid,
	                                      @RequestParam(required = false) Server.State state) {
		List<Server> servers = getServers(fileid, state);
		return servers;
	}
	
}

package com.distribox.aps.services;

import java.util.*;

import com.distribox.aps.dto.FileDataDto;
import com.distribox.aps.zookeeper.LeaderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.distribox.aps.dto.RequestDto;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 
 * Service for File Sistribution Service
 * @author @yoberner
 *
 */

@Service
public class FDService {

	@Autowired
	private LeaderObserver leaderObserver;

	// TODO add body to request
	// TODO add error and succes codes

	/**
	 * 
	 * @param request
	 * @return list of new file servers
	 */
	public ArrayList<String> getNewServerList(RequestDto request) {
		// get list of file servers from database
		// return list of file servers

		String server = leaderObserver.getLeaderId();
		WebClient webClient = WebClient.create();

		return webClient
				.get()
				.uri(server + "/serverids")
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<ArrayList<String>>(){})
				.block();
	}

	/**
	 * 
	 * @param request
	 * @return list of file servers
	 */
	public List<String> getServerList(RequestDto request) {
		// get list of file servers from database
		// return list of file servers

		String server = leaderObserver.getLeaderId();
		WebClient webClient = WebClient.create();

		return webClient
				.put()
				.uri(server + "/getFileList")
				.bodyValue(request.getUserId() + "/" + request.getFilePath() + "/" + request.getFileName())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<String>>(){})
				.block();
	}

	/**
	 * 
	 * @param request
	 * @return list of files
	 */
	public List<String> getFileList(RequestDto request) {
		// get list of files from database
		// return list of files

		// String server = "http://localhost:8081/getFileList";
		// WebClient webClient = WebClient.create();
		// ArrayList<String> serverList = webClient
		// 		.get()
		// 		.uri(server)
		// 		.retrieve().bodyToMono(new ParameterizedTypeReference<ArrayList<String>>() {
		// 		}).block();

		// return serverList;
		return List.of("http://localhost:8081");
	}

}

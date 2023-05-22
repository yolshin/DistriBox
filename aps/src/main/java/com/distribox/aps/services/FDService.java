package com.distribox.aps.services;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.distribox.aps.dto.FileDataDto;
import com.distribox.aps.dto.ResponseDto;
import com.distribox.aps.zookeeper.LeaderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.distribox.aps.dto.RequestDto;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 
 * Service for File Distribution Service
 * @author @yoberner
 *
 */

@Service
public class FDService {

	@Autowired
	private LeaderObserver leaderObserver;

	// TODO add body to request
	// TODO add error and success codes

	/**
	 * 
	 * @param request
	 * @return list of new file servers
	 */
	public ArrayList<ResponseDto> getNewServerList(RequestDto request) {
		// get list of file servers from database
		// return list of file servers

		String server = leaderObserver.getLeaderId();
		WebClient webClient = WebClient.create();

		ArrayList<ResponseDto> serverList = null;
		int retryCount = 0;
		int maxRetries = 3;

		while (retryCount < maxRetries && (serverList == null || serverList.size() < 3)) {
			serverList = webClient
					.get()
					.uri(server + "/servers")
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<ArrayList<ResponseDto>>() {})
					.block();

			if (serverList == null || serverList.size() < 3) {
				// Retry after 5 seconds
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					// Handle the exception
				}
				retryCount++;
			}
		}

		assert serverList != null;
//		if (serverList.size() < 3) {
//			return null;
//		}

		return (ArrayList<ResponseDto>) serverList.stream()
				.sorted(Comparator.comparing((ResponseDto obj) -> obj.getState().equals("OPEN") ? 0 : 1)
						.thenComparing(ResponseDto::getState))
				.collect(Collectors.toList());
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
				.post()
				.uri(server + "/getFileList")
				.bodyValue(request.getUserId() + "/" + request.getFilePath() + "/" + request.getFileName())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<String>>(){})
				.block();
	}

	public List<String> getServerList(String request) {
		// get list of file servers from database
		// return list of file servers

		String server = leaderObserver.getLeaderId();
		WebClient webClient = WebClient.create();

		return webClient
				.put()
				.uri(server + "/getFileList")
				.bodyValue(request)
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

	public ArrayList<String> reputFile(List<String> files) {

		ArrayList<ResponseDto> serverList = getNewServerList(new RequestDto());
		ArrayList<String> filteredServers = new ArrayList<>();

		for (String file: files) {
			List<String> servers = getServerList(file);

			filteredServers = serverList
					.stream()
					.map(ResponseDto::getId)
					.filter(id -> !servers.contains(id))
					.collect(Collectors.toCollection(ArrayList::new));

		}
		return filteredServers;
	}

}

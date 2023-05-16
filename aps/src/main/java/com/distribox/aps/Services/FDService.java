package com.distribox.aps.Services;

import java.util.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.distribox.aps.dto.RequestDto;

/**
 * 
 * Service for File Sistribution Service
 * @author @yoberner
 *
 */

@Service
public class FDService {

	// TODO add body to request
	// TODO add error and succes codes

	/**
	 * 
	 * @param request
	 * @return list of new file servers
	 */
	public List<String> getNewServerList(RequestDto request) {
		// get list of file servers from database
		// return list of file servers

//		String server = "http://localhost:8081/getNewServerList";
//		WebClient webClient = WebClient.create();
//		ArrayList<String> serverList = webClient
//				.get()
//				.uri(server)
//				.retrieve().bodyToMono(new ParameterizedTypeReference<ArrayList<String>>() {
//				}).block();

		return List.of("http://localhost:8081");
	}

	/**
	 * 
	 * @param request
	 * @return list of file servers
	 */
	public List<String> getServerList(RequestDto request) {
		// get list of file servers from database
		// return list of file servers

//		String server = "http://localhost:8081/getServerList";
//		WebClient webClient = WebClient.create();
//		ArrayList<String> serverList = webClient
//				.get()
//				.uri(server)
//				.retrieve().bodyToMono(new ParameterizedTypeReference<ArrayList<String>>() {
//				}).block();

		return List.of("http://localhost:8081");
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

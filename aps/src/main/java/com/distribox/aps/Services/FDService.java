package com.distribox.aps.Services;

import java.util.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 
 * Service for File Sistribution Service
 * @author @yoberner
 *
 */

@Service
public class FDService {

	// TODO add body to request

	/**
	 * 
	 * @param fileName
	 * @return list of new file servers
	 */
	public ArrayList<String> getNewServerList(String fileName) {
		// get list of file servers from database
		// return list of file servers
		String server = "http://localhost:8081/getNewServerList";
		WebClient webClient = WebClient.create();
		ArrayList<String> serverList = webClient
				.get()
				.uri(server)
				.retrieve().bodyToMono(new ParameterizedTypeReference<ArrayList<String>>() {
				}).block();

		return serverList;
	}

	/**
	 * 
	 * @param fileName
	 * @return list of file servers
	 */
	public ArrayList<String> getServerList(String fileName) {
		// get list of file servers from database
		// return list of file servers
		String server = "http://localhost:8081/getServerList";
		WebClient webClient = WebClient.create();
		ArrayList<String> serverList = webClient
				.get()
				.uri(server)
				.retrieve().bodyToMono(new ParameterizedTypeReference<ArrayList<String>>() {
				}).block();

		return serverList;
	}

	/**
	 * 
	 * @return list of files
	 */
	public ArrayList<String> getFileList() {
		// get list of files from database
		// return list of files
		String server = "http://localhost:8081/getFileList";
		WebClient webClient = WebClient.create();
		ArrayList<String> serverList = webClient
				.get()
				.uri(server)
				.retrieve().bodyToMono(new ParameterizedTypeReference<ArrayList<String>>() {
				}).block();

		return serverList;
	}

}

package com.distribox.aps.Services;

import java.util.*;

import com.distribox.aps.dto.RequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 
 * Service for File System Service
 * @author @yoberner
 *
 */

@Service
public class FSService {

	// TODO add body to request

	/**
	 * 
	 * @param servers
	 * @param request
	 * @return success message
	 */
	public String saveFile(List<String> servers, RequestDto request) {
		// send file to server
		// return success message
		for (String server : servers) {
			ObjectMapper objectMapper = new ObjectMapper();
			String requestBodyJson = null;
			try {
				requestBodyJson = objectMapper.writeValueAsString(request);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			// send file to server
			WebClient webClient = WebClient.create();
			// Handle the response body
			String ack = webClient
					.post()
					.uri(server + "/save")
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(requestBodyJson))
					.retrieve()
					.bodyToMono(String.class)
					.subscribe(System.out::println)
					.toString();

//			WebClient webClient = WebClient.create();
//			webClient.post()
//					.uri("http://example.com/api/resource")
//					.contentType(MediaType.APPLICATION_JSON)
//					.body(BodyInserters.fromValue(requestBodyJson))

		}
		return "File sent!";
	}

	/**
	 * 
	 * @param servers
	 * @param request
	 * @return File
	 */
	public String getFile(List<String> servers, RequestDto request) {
		// get file from server
		// return file
		for (String server : servers) {
			ObjectMapper objectMapper = new ObjectMapper();
			String requestBodyJson = null;
			try {
				requestBodyJson = objectMapper.writeValueAsString(request);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			// send file to server
			WebClient webClient = WebClient.create();
			// Handle the response body
			String file = webClient
					.post()
					.uri(server + "/get")
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(requestBodyJson))
					.retrieve()
					.bodyToMono(String.class)
					.block();
			if (file != null) {
				return file;
			}

		}
		return "File Not Found!";
	}

	/**
	 * 
	 * @param servers
	 * @param request
	 * @return success message
	 */
	public String deleteFile(List<String> servers, RequestDto request) {
		// delete file from servers
		// return success message
		for (String server : servers) {
			// delete file from server
			WebClient webClient = WebClient.create();
			String ack = webClient
					.delete()
					.uri(server + "/deleteFile")
					.retrieve().bodyToMono(String.class).block();

		}
		return "File deleted!";
	}

	/**
	 * 
	 */
	// public String renameFile(List<String> servers, String fileName) {
	// 	// rename file on servers
	// 	// rename by delete and create?
	// 	// return success message
	// 	for (String server : servers) {
	// 		// rename file on server
	// 		WebClient webClient = WebClient.create();
	// 		String ack = webClient
	// 				.put()
	// 				.uri(server + "/renameFile")
	// 				.retrieve().bodyToMono(String.class).block();

	// 	}
	// 	return "File renamed!";
	// }

}

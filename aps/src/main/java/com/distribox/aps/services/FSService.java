package com.distribox.aps.services;

import java.util.*;

import com.distribox.aps.dto.FileDataDto;
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

	// TODO add error and success codes
	// TODO add delete method

	/**
	 * 
	 * @param servers
	 * @param request
	 * @return success message
	 */
	public String saveFile(List<String> servers, RequestDto request) {
		// send file to server
		// return success message
		int counter = 0;
		String returnAcks = "";
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
					.block();

//			WebClient webClient = WebClient.create();
//			webClient.post()
//					.uri("http://example.com/api/resource")
//					.contentType(MediaType.APPLICATION_JSON)
//					.body(BodyInserters.fromValue(requestBodyJson))

			returnAcks += ack + ", ";
			if (ack != null) { //? Does this work?
				counter++;
			}
			if (counter == 3) {
				return "File sent! To " + counter + " servers: Acks:    " + returnAcks;
			}
		}
		if (counter < 3) {
			return "File not saved! Only saved on " + counter + " servers! Try again!";
		}
		return "File sent! To " + counter + " servers: Acks:    " + returnAcks;
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

			if (file != null) { //? Does this work?
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

			ObjectMapper objectMapper = new ObjectMapper();
			String requestBodyJson = null;
			try {
				requestBodyJson = objectMapper.writeValueAsString(request);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}

			// delete file from server
			WebClient webClient = WebClient.create();
			String ack = webClient
					.post()
					.uri(server + "/delete")
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(requestBodyJson))
					.retrieve()
					.bodyToMono(String.class)
					.block();

			if (ack == null) { //? Does this work?
				return "File not deleted from all servers! Try again!";
			}
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

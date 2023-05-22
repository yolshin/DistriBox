package com.distribox.aps.services;

import java.util.*;
import java.util.stream.Collectors;

import com.distribox.aps.dto.FileDataDto;
import com.distribox.aps.dto.FileDto;
import com.distribox.aps.dto.RequestDto;
import com.distribox.aps.dto.ResponseDto;
import com.distribox.aps.zookeeper.LeaderObserver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

/**
 * 
 * Service for File System Service
 * @author @yoberner
 *
 */

@Service
@Slf4j
public class FSService {

	// TODO add error and success codes
	// TODO add delete method

	@Autowired
	private FDService fdService;

	@Autowired
	private LeaderObserver leaderObserver;



	/**
	 * 
	 * @param servers
	 * @param request
	 * @return success message
	 */
	public String saveFile(List<ResponseDto> servers, RequestDto request) {
		// send file to server
		// return success message
		int counter = 0;
		String returnAcks = "";
		for (ResponseDto server : servers) {
			ObjectMapper objectMapper = new ObjectMapper();
			String requestBodyJson = null;
			try {
				requestBodyJson = objectMapper.writeValueAsString(request);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			// send file to server
			WebClient webClient = WebClient.create();
			String ack = "";
			// Handle the response body

			try {
				ack = webClient
					.post()
					.uri(server.getId() + "/save")
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(requestBodyJson))
					.retrieve()
					.bodyToMono(String.class)
					.block();
				// Connection succeeded
			} catch (WebClientRequestException e) {
				// Check for connection refused exception
//				if (e.getCause() instanceof java.net.ConnectException) {
					// Connection refused
					log.info("Server " + server + " is down!");
					Set<Object> files = serverDown(server.getId());
					for (Object file1 : files){
						Map<String,Object> file2 = (Map<String,Object>) file1;
						String filepath = (String) file2.get("filepath");
						List<Map<String,Object>> servers1 = (List<Map<String,Object>>) file2.get("servers");
						List<String> servers2 = servers1
								.stream()
								.map(s -> (String) s.get("id"))
								.filter(Objects::nonNull)
								.collect(Collectors.toList());
						log.info("Reputting file " + filepath + " to servers " + servers2);
						reputFile(filepath, servers2);
					}
//				} else {
//					// Handle other exceptions
//					System.out.println("Other exception occurred: " + Arrays.toString(e.getStackTrace()));
//				}
			}

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

	public Set<Object> serverDown(String server) {

		Map<String,String> body = new HashMap<String,String>();

		body.put("server", server);

		WebClient webClient = WebClient.create();

		Set<Object> files = webClient.post()
			.uri(leaderObserver.getLeaderId() + "/serverDown")
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(body))
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<Set<Object>>(){})
			.block();
		return files;
	}

	public String saveOneFile(List<String> servers, String file) {

		RequestDto request = new RequestDto();
		request.setUserId(file.substring(0,file.indexOf("/")));
		request.setFilePath(file.substring(file.indexOf("/")+1,file.lastIndexOf("/")));
		request.setFileName(file.substring(file.lastIndexOf("/")+1));

		List<String> list = fdService.getServerList(request);

		String contents = this.getFile(list, request);
		request.setFileContents(contents);


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
					.subscribe()
					.toString();

		}
		return "One file saved";
	}

	public ArrayList<String> reputFile(String filepath, List<String> servers) {

		ArrayList<ResponseDto> serverList = fdService.getNewServerList(new RequestDto());
		ArrayList<String> filteredServers = new ArrayList<>();

		for (String server: servers) {

			filteredServers = serverList
					.stream()
					.map(ResponseDto::getId)
					.filter(id -> !servers.contains(id))
					.collect(Collectors.toCollection(ArrayList::new));

			this.saveOneFile(filteredServers, filepath);

		}
		return filteredServers;
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
		String file = null;

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
			try {
			// Handle the response body
				file = webClient
						.post()
						.uri(server + "/get")
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(requestBodyJson))
						.retrieve()
						.bodyToMono(String.class)
						.block();
			} catch (WebClientRequestException e) {
				// Check for connection refused exception
//				if (e.getCause() instanceof java.net.ConnectException) {
					// Connection refused
					log.info("Server " + server + " is down!");
					Set<Object> files = serverDown(server);
					for (Object file1 : files){
						Map<String,Object> file2 = (Map<String,Object>) file1;
						String filepath = (String) file2.get("filepath");
						List<Map<String,Object>> servers1 = (List<Map<String,Object>>) file2.get("servers");
						List<String> servers2 = (List<String>) servers1
								.stream()
								.map(s -> (String) s.get("id"))
								.filter(Objects::nonNull)
								.collect(Collectors.toList());
						log.info("Reputting file " + filepath + " to servers " + servers2);
						reputFile(filepath, servers2);
					}
//				} else {
//					// Handle other exceptions
//					System.out.println("Other exception occurred: " + Arrays.toString(e.getStackTrace()));
//				}
			}



		}
		if (file != null) { //? Does this work?
			return file;
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

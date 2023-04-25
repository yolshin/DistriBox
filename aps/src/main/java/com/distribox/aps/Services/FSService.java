package com.distribox.aps.Services;

import java.util.*;

import org.springframework.stereotype.Service;
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
	 * @param fileName
	 * @param fileContents
	 * @return success message
	 */
	public String saveFile(List<String> servers, String fileName, String fileContents) {
		// send file to server
		// return success message
		for (String server : servers) {
			// send file to server
			WebClient webClient = WebClient.create();
			String ack = webClient
					.post()
					.uri(server + "/saveFile")
					.retrieve().bodyToMono(String.class).block();

		}
		return "File sent!";
	}

	/**
	 * 
	 * @param servers
	 * @param fileName
	 * @return File
	 */
	public String getFile(List<String> servers, String fileName) {
		// get file from server
		// return file
		for (String server : servers) {
			// get file from server
			WebClient webClient = WebClient.create();
			String file = webClient
					.get()
					.uri(server + "/getFile")
					.retrieve().bodyToMono(String.class).block();
			if (file != null) {
				return file;
			}

		}
		return "File Not Found!";
	}

	/**
	 * 
	 * @param servers
	 * @param fileName
	 * @return success message
	 */
	public String deleteFile(List<String> servers, String fileName) {
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

package com.distribox.aps.Services;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class FSService {
	
	public String saveFile(List<String> servers, String file) {
		// send file to server
		// return success message
		return "File sent!";
	}

	public String getFile(List<String> servers, String fileName) {
		// get file from server
		// return file
		return "File received!";
	}

	public String deleteFile(List<String> servers, String fileName) {
		// delete file from servers
		// return success message
		return "File deleted!";
	}

	public String renameFile(List<String> servers, String fileName) {
		// rename file on servers
		// return success message
		return "File renamed!";
	}

}

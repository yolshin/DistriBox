package com.distribox.aps.Services;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class FSService {
	
	public String sendFile(List<String> servers, String file) {
		// send file to server
		// return success message
		return "File sent!";
	}

	public String getFile(List<String> servers, String file) {
		// get file from server
		// return file
		return "File received!";
	}

}

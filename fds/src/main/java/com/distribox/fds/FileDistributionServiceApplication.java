package com.distribox.fds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileDistributionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileDistributionServiceApplication.class, args);
	}
	/*
	API calls I need:
		Application Service
		- GET servers
			- Returns servers that are open for work
				- (however I want to define it)
		- GET servers for file
			- For a /get call on a specific file, return servers that file is on
		FSS
		-  POST file-saved
			-  Sends an ID, path, user, servers
			-  Send ACK back
	*/
}

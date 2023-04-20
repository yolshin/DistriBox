package com.distribox.fds.controllers;

import com.distribox.fds.repos.*;
import com.distribox.fds.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloWorldController {

	@Autowired
	FilesRepository filesRepository;

	@Autowired
	UsersRepository usersRepository;
	private static final Logger log = LoggerFactory.getLogger(HelloWorldController.class);

	public void getFiles() {
		List<File> files = new ArrayList<>(filesRepository.findAll());
		for (File f : files) {
			log.info("File " + f.fileid);
		}
	}

	public void getUsers() {
		List<User> users = new ArrayList<>(usersRepository.findAll());
		for (User u: users) {
			log.info("User " + u.userid);
		}
	}
	@GetMapping("/hello2")
	public String hello() {
		getFiles();
		getUsers();
		return "HelloWorld2";
	}


}

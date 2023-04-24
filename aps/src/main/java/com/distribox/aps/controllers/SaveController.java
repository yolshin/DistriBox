package com.distribox.aps.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.Services.FDService;
import com.distribox.aps.Services.FSService;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class SaveController {

	@Autowired
	private FDService fdService;

	@Autowired
	private FSService fsService;

	@PostMapping(value="/save")
	public String saveFile(@RequestBody String file) {
		// get list of file servers to send to
		// send file to each file server
		// return success message
		ArrayList<String> serverList = fdService.getServerList();
		fsService.sendFile(serverList, file);
		return "File saved!";
	}

}

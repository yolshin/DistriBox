package com.distribox.aps.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.Services.FDService;
import com.distribox.aps.Services.FSService;

@RestController
public class GetController {

	@Autowired
	private FDService fdService;

	@Autowired
	private FSService fsService;

	@GetMapping(value="/get")
	public String getFile(@RequestBody String fileName) {
		// get list of file servers to get from
		// get from file from first server
		// return file
		ArrayList<String> serverList = fdService.getServerList(fileName);
		String file = fsService.getFile(serverList, fileName);
		return file;
	}

}

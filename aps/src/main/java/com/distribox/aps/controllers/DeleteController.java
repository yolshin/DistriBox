package com.distribox.aps.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.Services.FDService;
import com.distribox.aps.Services.FSService;

@RestController
public class DeleteController {

	@Autowired
	private FDService fdService;

	@Autowired
	private FSService fsService;
	
	@DeleteMapping(value="/delete")
	public String deleteFile(@RequestBody String fileName) {
		// get list of file servers to delete from
		// delete file from each file server
		// return success message
		ArrayList<String> serverList = fdService.getServerList(fileName);
		String ack = fsService.deleteFile(serverList, fileName);
		return ack;
	}

}

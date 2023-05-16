package com.distribox.aps.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.Services.FDService;
import com.distribox.aps.Services.FSService;
import com.distribox.aps.dto.RequestDto;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller for Save requests
 * @author @yoberner
 *
 */

@RestController
public class SaveController {

	@Autowired
	private FDService fdService;

	@Autowired
	private FSService fsService;

	@PostMapping(value="/save")
	public String saveFile(@RequestBody RequestDto request) {
		// get list of file servers to send to
		// send file to each file server
		// return success message

		// old way: depricated:
		// parse file name from string until comma:
		// String fileName = file.substring(0, file.indexOf(","));
		// String fileContents = file.substring(file.indexOf(",") + 1);

		// String userId = request.getUserId();
		// String filePath = request.getFilePath();
		// String fileName = request.getFileName();
		// String fileContents = request.getFileContents();

		List<String> serverList = fdService.getNewServerList(request);
		String ack = fsService.saveFile(serverList, request);
		return ack + "and Saved!";
	}

}

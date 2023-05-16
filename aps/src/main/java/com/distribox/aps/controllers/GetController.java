package com.distribox.aps.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.services.FDService;
import com.distribox.aps.services.FSService;
import com.distribox.aps.dto.RequestDto;

/**
 * Controller for Get requests
 * @author @yoberner
 *
 */

@RestController
public class GetController {

	@Autowired
	private FDService fdService;

	@Autowired
	private FSService fsService;

	@GetMapping(value="/get")
	public String getFile(@RequestBody RequestDto request) {
		// get list of file servers to get from
		// get from file from first server
		// return file

		// String userId = request.getUserId();
		// String filePath = request.getFilePath();
		// String fileName = request.getFileName();

		List<String> serverList = fdService.getServerList(request);
		String returnFile = fsService.getFile(serverList, request);
		return returnFile;
	}

}

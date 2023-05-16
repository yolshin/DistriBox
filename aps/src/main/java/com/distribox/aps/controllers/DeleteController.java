package com.distribox.aps.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.Services.FDService;
import com.distribox.aps.Services.FSService;
import com.distribox.aps.dto.RequestDto;

/**
 * Controller for Delete requests
 * @author @yoberner
 *
 */

@RestController
public class DeleteController {

	@Autowired
	private FDService fdService;

	@Autowired
	private FSService fsService;
	
	@DeleteMapping(value="/delete")
	public String deleteFile(@RequestBody RequestDto request) {
		// get list of file servers to delete from
		// delete file from each file server
		// return success message

		// String userId = request.getUserId();
		// String filePath = request.getFilePath();
		// String fileName = request.getFileName();

		List<String> serverList = fdService.getServerList(request);
		String ack = fsService.deleteFile(serverList, request);
		return "DELETE STATUS RESPONSE FROM SERVER: " + ack;
	}

}

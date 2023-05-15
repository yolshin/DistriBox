package com.distribox.aps.controllers;

import java.util.*;

import com.distribox.aps.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.Services.FDService;
import com.distribox.aps.Services.FSService;

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
	public String deleteFile(@RequestBody RequestDto fileName) {
		// get list of file servers to delete from
		// delete file from each file server
		// return success message
		List<String> serverList = fdService.getServerList(fileName);
		String ack = fsService.deleteFile(serverList, fileName);
		return ack + "and Removed!";
	}

}

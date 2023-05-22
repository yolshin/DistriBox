package com.distribox.aps.controllers;

import com.distribox.aps.dto.FileDataDto;
import com.distribox.aps.dto.ResponseDto;
import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.services.FDService;
import com.distribox.aps.services.FSService;
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

		List<ResponseDto> serverList = fdService.getNewServerList(request);
		if (serverList == null) {
			return "Not enough servers available. Please try again later.";
		}
		String ack = fsService.saveFile(serverList, request);
		return "SAVE STATUS RESPONSE FROM SERVER: " + ack;
	}

}

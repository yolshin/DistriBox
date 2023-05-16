package com.distribox.aps.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.Services.FDService;
import com.distribox.aps.dto.RequestDto;

/**
 * Controller for FileList requests
 * @author @yoberner
 *
 */

@RestController
public class FileListController {

	@Autowired
	private FDService fdService;

	@GetMapping(value="/filelist")
	public List<String> getFileList(@RequestBody RequestDto request) {
		// get list of file servers to get from
		// get from file from first server
		// return file

		// String userId = request.getUserId();

		List<String> fileList = fdService.getFileList(request);
		return fileList;
	}

}

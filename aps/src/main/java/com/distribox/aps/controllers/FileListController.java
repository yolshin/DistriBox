package com.distribox.aps.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distribox.aps.Services.FDService;

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
	public List<String> getFileList() {
		// get list of file servers to get from
		// get from file from first server
		// return file
		ArrayList<String> fileList = fdService.getFileList();
		return fileList;
	}

}

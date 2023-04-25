// package com.distribox.aps.controllers;

// import java.util.*;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.distribox.aps.Services.FDService;
// import com.distribox.aps.Services.FSService;

// /**
//  * Controller for Rename requests
//  * @author @yoberner
//  *
//  */

// @RestController
// public class RenameController {

// 	@Autowired
// 	private FDService fdService;

// 	@Autowired
// 	private FSService fsService;

// 	@PutMapping(value="/rename")
// 	public String renameFile(@RequestBody String fileName) {
// 		// get list of file servers to rename on
// 		// rename file on each file server
// 		// return success message
// 		ArrayList<String> serverList = fdService.getServerList(fileName);
// 		String ack = fsService.renameFile(serverList, fileName);
// 		return ack;
// 	}

// }

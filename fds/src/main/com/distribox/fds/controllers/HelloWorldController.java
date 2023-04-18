package com.distribox.fds.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	@GetMapping("/hello2")
	public String hello() {
		return "HelloWorld2";
	}


}

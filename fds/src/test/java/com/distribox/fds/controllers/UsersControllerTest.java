package com.distribox.fds.controllers;

import com.distribox.fds.SharedTests;
import com.distribox.fds.entities.File;
import com.distribox.fds.entities.User;
import com.distribox.fds.repos.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@PropertySource("classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersControllerTest extends SharedTests {
	private static final Logger log = LoggerFactory.getLogger(UsersControllerTest.class);


	@Autowired
	private UsersController usersController;

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	public TestRestTemplate restTemplate;

	@Value(value="${local.server.port}")
	public int port;

	@Test
	public void contextLoads() {
		assertNotNull(usersController);
	}

	@Test
	public void getUser() {
		ResponseEntity<Map> response = restTemplate.getForEntity("http://localhost:" + port + "/user?userid=mberk",
				Map.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		List<String> fileList = (List<String>) response.getBody().get("filepaths");
		User actualUser = usersRepository.findById("mberk").get();
		List<String> actualFiles = actualUser.getFiles().stream().map(File::getFilepath).toList();
		assertArrayEquals(actualFiles.toArray(), fileList.toArray());
	}
}

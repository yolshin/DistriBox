package com.distribox.fds.controllers;

import com.distribox.fds.entities.File;
import com.distribox.fds.entities.Server;
import com.distribox.fds.entities.User;
import com.distribox.fds.repos.FilesRepository;
import com.distribox.fds.repos.ServersRepository;
import com.distribox.fds.repos.UsersRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@PropertySource("classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServersControllerTest {

	@Autowired
	ServersRepository serversRepository;

	@Autowired
	FilesRepository filesRepository;
	@Autowired
	UsersRepository usersRepository;

	HashMap<String, Server> servers = new HashMap<>();

	@BeforeAll
	public void setupServers() {
		Server server = new Server();
		serversRepository.save(server);
		servers.put(server.getId().toString(), server);

	}

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	TestRestTemplate restTemplate;


	@Autowired
	private ServersController serversController;

	@Test
	public void contextLoads() {
		assertNotNull(serversController);
	}

	@Test
	public void serverListTest() {
		List<HashMap<String, String>> s = restTemplate.getForObject("http://localhost:" + port + "/servers",
				List.class);
		for (HashMap map: s) {
			String id = (String) map.get("id");
			assertTrue(servers.containsKey(id));
		}
	}

	void setupServerFileUser() {

	}

	@Test
	public void severListFilesTest() {
		User mberk = new User("mberk");
		User u2 = new User("u2");
		usersRepository.save(mberk);
		usersRepository.save(u2);
		Set<Server> serverSet = new HashSet<>(servers.values());
		File mFile1 = new File("stuff/file1.txt", mberk);
		File mFile2 = new File("stuff/file2.txt", mberk);
		File uFile = new File("f3.txt", u2);
		List<File> files = Arrays.asList(mFile1, mFile2, uFile);
		//TODO: Find a way to generate ID before a file is saved
		filesRepository.saveAll(files);
		for (File file: files) {
			file.addServers(serverSet);
		}
		filesRepository.saveAll(files);
		serversRepository.saveAll(serverSet);

		List<HashMap> response =
				restTemplate.getForObject("http://localhost:" + port + "/servers?fileid=" + mFile1.fileid.toString(),
						List.class);
		Set<String> serverIds = mFile1.servers.stream().map(s -> s.getId().toString()).collect(Collectors.toSet());
		Set<String> fetchedIds = response.stream().map(m -> (String) m.get("id")).collect(Collectors.toSet());
		assertEquals(serverIds, fetchedIds);
	}

	@AfterAll
	public void cleanup() {
		serversRepository.deleteAll();
		filesRepository.deleteAll();
		usersRepository.deleteAll();
	}


}
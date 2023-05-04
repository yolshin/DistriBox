package com.distribox.fds.controllers;

import com.distribox.fds.SharedTests;
import com.distribox.fds.entities.File;
import com.distribox.fds.entities.Server;
import com.distribox.fds.entities.User;
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
class ServersControllerTest extends SharedTests {

	@Autowired
	private ServersController serversController;
	@Autowired
	public TestRestTemplate restTemplate;

	@Value(value="${local.server.port}")
	public int port;

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
		}
	}


//	@Transactional
	public List<File> serverListFilesTransaction() {
		User mberk = usersRepository.getReferenceById("mberk");
		File mFile1 = new File("stuff/file1.txt", mberk);
		File mFile2 = new File("stuff/file2.txt", mberk);
		List<File> files = Arrays.asList(mFile1, mFile2);
		Server s2 = new Server();
		saveServers(List.of(s2));
		servers.add(s2);

		files = saveFiles(files);
		List<Server> serverList = servers.stream().toList();
		Server s1 = serverList.get(0);
		s1.addFile(mFile1);
		s2.addFile(mFile2);
		files = saveFiles(files);
		serverList = saveServers(serverList);

		serverList = saveServers(serverList);

		filesRepository.saveAll(Arrays.asList(mFile1, mFile2));
		filesRepository.saveAll(Arrays.asList(mFile1, mFile2));
		serverList = saveServers(serverList);
		return filesRepository.saveAll(files);
	}
	public List<Server> saveServers(List<Server> servers) {
		return serversRepository.saveAll(servers);
	}
	public List<File> saveFiles(List<File>fileList) {
		return filesRepository.saveAll(fileList);
	}


	@Test
	public void serverListFilesTest() {
		List<File> files = serverListFilesTransaction();
		File mFile1 = files.get(0);
		ResponseEntity<List> response =
				restTemplate.getForEntity("http://localhost:" + port + "/servers?fileid=" + mFile1.fileid.toString(),
						List.class);
		List<HashMap<String, Object>> list = response.getBody();
		int port = 8000;
		System.out.println(response);
		Set<String> serverIds = mFile1.getServers().stream().map(s -> s.getId().toString()).collect(Collectors.toSet());
		Set<String> fetchedIds = list.stream().map(m -> (String) m.get("id")).collect(Collectors.toSet());
		assertEquals(serverIds, fetchedIds);
	}

	@Test
	public void saveFileTest() {
		Map<String, Object> body = new HashMap<>();
		String filepath = "/saveFileTest/file1.txt";
		body.put("filepath", filepath);
		body.put("userid", "sftUser");
		List<String> serverids = servers.stream().map(s -> s.getId().toString()).toList();
		body.put("serverids", serverids);
		String response = restTemplate.postForObject("http://localhost:" + port + "/saveFile", body, String.class);
		User u = usersRepository.findByUserid("sftUser");
		Set<String> filepaths = u.files.stream().map(f -> f.filepath).collect(Collectors.toSet());
		assertTrue(filepaths.contains(filepath));
	}


}
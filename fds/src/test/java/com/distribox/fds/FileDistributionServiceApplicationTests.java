package com.distribox.fds;

import com.distribox.fds.entities.*;
//import com.distribox.fds.services.*;
import com.distribox.fds.repos.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@PropertySource("classpath:application.properties")
class FileDistributionServiceApplicationTests {

	@Autowired
	FilesRepository filesRepository;
	@Autowired
	UsersRepository usersRepository;

	@Autowired
	ServersRepository serversRepository;

	List<File> files;
	List<User> users;

	@BeforeAll
	public void setupDB() {
		Server s1 = new Server();
		serversRepository.save(s1);
		users = new ArrayList<>();
		users.add(new User("benE"));
		users.add(new User("mberk"));
		users.add(new User("yberner"));
		users.add(new User("yolshin"));

		users.forEach(usersRepository::save);

		List<Integer> servers = Arrays.asList(0, 1, 2);
		files = new ArrayList<>();
		files.add(new File("benFile", "benE"));
		files.add(new File("file1", "mberk"));
		files.add(new File("bernerFile", "yberner"));
		files.add(new File("olFile", "yolshin"));
		files.forEach(filesRepository::save);
		files.forEach(f -> f.addServer(s1));

		files.forEach(filesRepository::save);
		serversRepository.save(s1);
	}

	@Test
	public void basicTest() {
		List<File> actualFiles = new ArrayList<>(filesRepository.findAll());
		List<User> actualUsers = new ArrayList<>(usersRepository.findAll());
		List<Server> servers = new ArrayList<>(serversRepository.findAll());
		assertArrayEquals(actualFiles.toArray(), files.toArray());
		actualUsers.sort(Comparator.comparing(u -> u.userid));
		assertArrayEquals(actualUsers.toArray(), users.toArray());
		assertEquals(1, servers.size());
		for (User u : actualUsers) {
			assertEquals(u.files.size(), 1);
		}
		Server s1 = servers.get(0);
		for (int i = 0; i < actualFiles.size(); i++) {
			File f = actualFiles.get(i);
			assertEquals(f.user, actualUsers.get(i));
			assertTrue(f.servers.contains(s1));
		}
		actualFiles.forEach(f -> f.servers.remove(s1));
		for (File f: actualFiles) {
			assertEquals(f.servers.size(), 0);
		}

	}

	@AfterAll
	public void cleanup() {
		serversRepository.deleteAll();
		filesRepository.deleteAll();
		usersRepository.deleteAll();
	}

}

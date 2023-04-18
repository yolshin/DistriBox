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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FDSTests {

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
		files.add(new File("benFile", s1, "benE"));
		files.add(new File("file1", s1, "mberk"));
		files.add(new File("bernerFile", s1, "yberner"));
		files.add(new File("olFile", s1, "yolshin"));

		files.forEach(filesRepository::save);

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
		for (int i = 0; i < actualFiles.size(); i++) {
			File f = actualFiles.get(i);
			assertEquals(f.user, actualUsers.get(i));
			assertEquals(f.server, servers.get(0));
		}

		List<File> serverFiles = servers.get(0).getFiles();
		serverFiles.sort(Comparator.comparing(f -> f.filepath));
		actualFiles.sort(Comparator.comparing(f -> f.filepath));
		assertArrayEquals(actualFiles.toArray(), serverFiles.toArray());


	}

	@AfterAll
	public void cleanup() {
		filesRepository.deleteAll();
		usersRepository.deleteAll();
		serversRepository.deleteAll();
	}

}

package com.distribox.fds;

import com.distribox.fds.entities.*;
//import com.distribox.fds.services.*;
import com.distribox.fds.repos.FilesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = FilesRepository.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@PropertySource("classpath:application.properties")
//@ExtendWith(SpringExtension.class)
//@ContextHierarchy({
//		@ContextConfiguration(classes = SharedTests.class)
//})

class Thing2 {
	@Autowired
	FilesRepository filesRepository;
}

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileDistributionServiceApplicationTests extends SharedTests {

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
			assertTrue(f.getServers().contains(s1));
		}
		actualFiles.forEach(f -> f.getServers().remove(s1));
		for (File f : actualFiles) {
			assertEquals(f.getServers().size(), 0);
		}
	}
}

package capstone.distribox.project;

import capstone.distribox.project.services.*;
import capstone.distribox.project.repos.*;
import capstone.distribox.project.entities.*;
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
class DistriBoxApplicationTests {

	@Autowired
	FileService fileService;

	@Autowired
	FilesRepository filesRepository;
	@Autowired
	UsersRepository usersRepository;

	List<File> files;
	List<User> users;

	//
	@BeforeAll
	public void setupDB() {
		users = new ArrayList<>();
		users.add(new User("benE"));
		users.add(new User("mberk"));
		users.add(new User("yberner"));
		users.add(new User("yolshin"));

		users.forEach(usersRepository::save);

		files = new ArrayList<>();
		files.add(new File("benFile", 0, "benE"));
		files.add(new File("file1", 0, "mberk"));
		files.add(new File("bernerFile", 0, "yberner"));
		files.add(new File("olFile", 0, "yolshin"));

		files.forEach(filesRepository::save);
	}

	@Test
	public void basicTest() {
		List<File> actualFiles = new ArrayList<>(filesRepository.findAll());
		List<User> actualUsers = new ArrayList<>(usersRepository.findAll());
		assertArrayEquals(actualFiles.toArray(), files.toArray());
		actualUsers.sort(Comparator.comparing(u -> u.userid));
		assertArrayEquals(actualUsers.toArray(), users.toArray());
		for (User u: actualUsers) {
//			assertEquals(u.files.size(), 1);
		}
		for (int i = 0; i < actualFiles.size(); i++) {
			File f = actualFiles.get(i);
			assertEquals(f.user, actualUsers.get(i));
		}

	}

	@AfterAll
	public void cleanup() {
		filesRepository.deleteAll();
		usersRepository.deleteAll();
	}

}

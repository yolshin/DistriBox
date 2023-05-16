package com.distribox.fds.repos;

import com.distribox.fds.entities.File;
import com.distribox.fds.entities.Server;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServersRepository extends JpaRepository<Server, String> {

	List<Server> findByState(Server.State state, Sort sort);

//	List<Server> findByFiles_fileid(UUID fileid, Sort sort);

	List<Server> findByFiles_filepath(String filepath, Sort sort);

//	List<Server> findByStateAndFiles_fileid(Server.State state, UUID fileid, Sort sort);
	List<Server> findByStateAndFiles_filepath(Server.State state, String filepath, Sort sort);

	List<Server> findByFiles(File file);

}
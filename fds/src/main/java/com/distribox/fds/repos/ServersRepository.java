package com.distribox.fds.repos;

import com.distribox.fds.entities.File;
import com.distribox.fds.entities.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServersRepository extends JpaRepository<Server, UUID> {

	Optional<Server> findById(UUID id);

	List<Server> findByState(Server.State state);
	List<Server> findByFiles_fileid(UUID fileid);

	List<Server> findByStateAndFiles_fileid(Server.State state, UUID fileid);
	List<Server> findByFiles(File file);
}
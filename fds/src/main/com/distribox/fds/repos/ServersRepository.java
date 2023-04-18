package com.distribox.fds.repos;

import com.distribox.fds.entities.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServersRepository extends JpaRepository<Server, UUID> {

	Optional<Server> findById(UUID id);
}
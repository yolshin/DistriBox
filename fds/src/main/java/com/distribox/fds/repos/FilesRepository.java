package com.distribox.fds.repos;

import com.distribox.fds.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilesRepository extends JpaRepository<File, UUID> {
	Optional<File> findByFilepath(String id);
}

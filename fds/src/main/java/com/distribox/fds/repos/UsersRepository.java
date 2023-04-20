package com.distribox.fds.repos;

import com.distribox.fds.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {

	Optional<User> findById(String id);
}

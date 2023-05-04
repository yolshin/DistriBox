package com.distribox.fds.repos;

import com.distribox.fds.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {
	boolean existsByUseridLike(String userid);

	Optional<User> findById(String id);

	@Query("select u from User u where u.userid = ?1")
	User findByUserid(String userid);


}

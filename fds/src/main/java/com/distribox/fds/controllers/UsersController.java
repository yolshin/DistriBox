package com.distribox.fds.controllers;

import com.distribox.fds.entities.File;
import com.distribox.fds.entities.User;
import com.distribox.fds.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping ("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserFiles(String userid) {
        //check if user exists
        Optional<User> userOptional = usersRepository.findById(userid);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        return ResponseEntity.ok(user);
    }

}

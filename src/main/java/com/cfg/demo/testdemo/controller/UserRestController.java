package com.cfg.demo.testdemo.controller;

import com.cfg.demo.testdemo.model.UserDetails;
import com.cfg.demo.testdemo.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @PostMapping(value = "/user/save",consumes = "application/json")
    public ResponseEntity<UserDetails> saveUserDetails(@RequestBody UserDetails userDetails){
        UserDetails saved=userDetailsRepository.save(userDetails);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}

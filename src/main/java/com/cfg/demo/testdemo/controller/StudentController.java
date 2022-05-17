package com.cfg.demo.testdemo.controller;

import com.cfg.demo.testdemo.model.Student;
import com.cfg.demo.testdemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @PostMapping(value = "/student/save",produces = "application/json")
    public ResponseEntity<Student> saveStudent(@RequestBody Student student){
        Student s=studentRepository.save(student);
        return new ResponseEntity<>(s, HttpStatus.CREATED);
    }
}

package com.cfg.demo.testdemo;

import com.cfg.demo.testdemo.controller.StudentController;
import com.cfg.demo.testdemo.model.Student;
import com.cfg.demo.testdemo.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TestStudentController {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void checkRepository(){
        Annotation annotation= AnnotationUtils.findAnnotation(StudentRepository.class, Repository.class);
        boolean hasRepository=false;
        if(annotation!=null)
            hasRepository=true;
        assertTrue(hasRepository);
    }

    @Test
    public void checkEntity(){
        Annotation annotation=AnnotationUtils.findAnnotation(Student.class, Entity.class);
        boolean hasEntity=false;
        if(annotation!=null)
            hasEntity=true;
        assertTrue(hasEntity);
    }

    @Test
    public void checkController(){
        Annotation annotation=AnnotationUtils.findAnnotation(StudentController.class, RestController.class);
        boolean hasController=false;
        if(annotation!=null)
            hasController=true;
        assertTrue(hasController);
    }

    @Test
    public void testSaveStudent() throws Exception {
        ObjectMapper objectMapper=new ObjectMapper();
        Student student=new Student(1,"Raghav","Bengaluru");
        MockHttpServletResponse response=mockMvc.perform(post("/student/save")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(student)))
                .andDo(print())
                .andExpect(jsonPath("$.studentName").value("Raghav"))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        Integer id= JsonPath.parse(response.getContentAsString()).read("$.studentId");
        assertEquals(1,id);
    }

}

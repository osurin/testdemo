package com.cfg.demo.testdemo;

import com.cfg.demo.testdemo.controller.UserRestController;
import com.cfg.demo.testdemo.model.UserDetails;
import com.cfg.demo.testdemo.repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import java.lang.annotation.Annotation;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class TestdemoApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void checkEntity(){
		boolean hasEntity=false;
		Annotation annotation= AnnotationUtils.findAnnotation(UserDetails.class,Entity.class);
		if(annotation!=null)
			hasEntity=true;
		assertTrue(hasEntity);
	}

	@Test
	public void checkRestController(){
		boolean hasRestController=true;
		Annotation annotation=AnnotationUtils.findAnnotation(UserRestController.class, RestController.class);
		if(annotation!=null)
			hasRestController=true;
		assertTrue(hasRestController);

	}
	@Autowired
	UserDetailsRepository userDetailsRepository;
	@Test
	public void testUserDetailsSaveRepository(){
		UserDetails userDetails=new UserDetails(1001,"narayana",true);
		UserDetails saved=userDetailsRepository.save(userDetails);
		assertTrue(saved.getActive());
	}
	@Test
	public void testSaveUserDetails() throws Exception{
		UserDetails userDetails=new UserDetails(2001,"Ravi",false);
		MockHttpServletResponse response=mockMvc.perform(post("/user/save")
				.contentType("application/json")
				.content(new ObjectMapper().writeValueAsString(userDetails)))
				.andDo(print())
				.andExpect(jsonPath("$.*",hasSize(3)))
				.andExpect(jsonPath("$.userId").value("2001"))
				.andExpect(jsonPath("$.userName").value("Ravi"))
				.andExpect(jsonPath("$.active").value(false))
				.andExpect(status().isCreated())
				.andReturn().getResponse();
		Integer id= JsonPath.parse(response.getContentAsString()).read("$.userId");
		System.out.println("id:"+id);
		assertEquals(2001,id);
	}
	@Test
	void contextLoads() {
	}

}

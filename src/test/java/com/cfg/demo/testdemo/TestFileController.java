package com.cfg.demo.testdemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestFileController {

    public static final String BASE="data";
    @Autowired(required = true)
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setUp(){
        File file=new File(BASE);
        if(file.exists() && file.isDirectory()){
            Arrays.asList(file.listFiles()).forEach(f->f.delete());
        }else{
            file.mkdir();
        }
    }

    @Test
    public void testUpload(){
        String fileName="testfile.txt";
        File uploadFile= Paths.get(BASE+fileName).toFile();
        byte[] file=new byte[1024*10];
        ResponseEntity<Void> response=testRestTemplate.postForEntity("/uploader",prepareRequest(fileName,file),Void.class);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test
    public void testDownload(){
        String fileName="download.txt";
        File uploadFile=Paths.get(BASE+fileName).toFile();
        byte[] file=new byte[1024*10];
        ResponseEntity<Void> response=testRestTemplate.postForEntity("/uploader",prepareRequest(fileName,file),Void.class);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());

        HttpHeaders header=new HttpHeaders();
        header.setAccept(Arrays.asList(MediaType.ALL));
        HttpEntity<String> entity=new HttpEntity<>(header);
        ResponseEntity<byte[]> responseEntity=testRestTemplate.exchange("/download?fileName="+fileName,HttpMethod.GET,entity,byte[].class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());


    }

    private HttpEntity<MultiValueMap<String,Object>> prepareRequest(String fileName,byte[] file){
        MultiValueMap<String,String> fileInfo=new LinkedMultiValueMap<>();
        ContentDisposition fileDetails= ContentDisposition.builder("form-data")
                .filename(fileName)
                .name("file")
                .build();
        fileInfo.add(HttpHeaders.CONTENT_DISPOSITION,fileDetails.toString());
        HttpEntity<byte[]> fileEntity=new HttpEntity<>(file,fileInfo);
        MultiValueMap<String,Object> parameters=new LinkedMultiValueMap<>();
        if(fileName!=null)
            parameters.add("fileName",fileName);
        if(file!=null)
            parameters.add("file",fileEntity);
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String,Object>> httpEntity=new HttpEntity<>(parameters,headers);
        return httpEntity;
    }

}

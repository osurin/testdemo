package com.cfg.demo.testdemo.controller;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class FileController {
    public static final String UPLOAD_DIR="uploads/";

    @PostMapping(value = "/uploader", produces = "application/json")
    public ResponseEntity fileUpload(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file) throws IOException {
        File newFile=new File(UPLOAD_DIR+""+fileName);
        System.out.println(newFile.getAbsolutePath());
        newFile.createNewFile();
        FileOutputStream fileOutputStream=new FileOutputStream(newFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/download", produces = "application/json")
    public ResponseEntity downloadFile(@RequestParam String fileName){
        File file=new File(UPLOAD_DIR+fileName);
        if(file.exists()){
            try {
                org.springframework.core.io.Resource resource=new UrlResource(file.toURI());
                return new ResponseEntity(resource,HttpStatus.OK);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}

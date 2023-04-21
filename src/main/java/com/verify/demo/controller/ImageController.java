package com.verify.demo.controller;

import com.verify.demo.domain.Image;
import com.verify.demo.domain.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        Image imageEntity = new Image(file.getOriginalFilename(),file.getBytes());
        imageRepository.save(imageEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body("Image uploaded and saved.");
    }


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        Image imageEntity = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));

        byte[] imageData = imageEntity.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageData.length);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }


}

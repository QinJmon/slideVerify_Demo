package com.verify.demo.controller;

import com.verify.demo.domain.Image;
import com.verify.demo.domain.PuzzleResponse;
import com.verify.demo.service.ImageService;
import com.verify.demo.util.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PuzzleController {

    private final ImageService imageService;

    //生成原图、拼图块和正确拖动位置
    @GetMapping("/generatePuzzle")
    public ResponseEntity<PuzzleResponse> generatePuzzle() throws IOException {
        PuzzleResponse puzzleResponse = imageService.generatePuzzleData();
        System.out.println(puzzleResponse);

        //将编码后的图片数据解码为 BufferedImage 对象，并将其保存到本地文件中
        String encodedImage = puzzleResponse.getPuzzleImage();
        byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        File output = new File(System.getProperty("user.home") + "/Desktop/puzzle.png");
        ImageIO.write(bufferedImage, "png", output);



        return ResponseEntity.ok(puzzleResponse);

    }



    //验证拖动结果
    @PostMapping("/verifyPuzzle")
    public Boolean verifyPuzzle(@RequestBody Map<String, String> data) {
        String movedPosition = data.get("movedPosition");
        String encryptedPosition = data.get("encryptedPosition");

        return imageService.varify(movedPosition,encryptedPosition);

    }
}

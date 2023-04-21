package com.verify.demo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Getter
public class PuzzleResponse {
    private  String originalImage;
    private  String puzzleImage;
//    private final String puzzleBackground;
    private  String encryptedPosition;
    

    public PuzzleResponse(BufferedImage originalImage, BufferedImage puzzleImage, String encryptedPosition) throws IOException {
        this.originalImage = encodeImageToBase64(originalImage);
        this.puzzleImage = encodeImageToBase64(puzzleImage);
//        this.puzzleBackground = encodeImageToBase64(puzzleBackground);
        this.encryptedPosition = encryptedPosition;
    }

    private String encodeImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());

    }
}

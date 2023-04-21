package com.verify.demo.service;

import com.verify.demo.domain.Image;
import com.verify.demo.domain.ImageRepository;
import com.verify.demo.domain.PuzzleResponse;
import com.verify.demo.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Value("${AES.encryptionKey}")
    private String encryptionKey;

    private static final int PUZZLE_PIECE_WIDTH = 50;
    private static final int PUZZLE_PIECE_HEIGHT = 50;

    private static final int IMAGE_WIDTH =6;
    private static final int IMAGE_HEIGHT=6;


    public PuzzleResponse generatePuzzleData() throws IOException {
        //从数据库中随机抽取一个image
        Image randomImage = imageRepository.findRandomImage();
        byte[] imageData = randomImage.getData();
        BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(imageData));

        // 创建一个用于存放拼图碎片的 BufferedImage 对象
        BufferedImage puzzlePiece = new BufferedImage(
                inputImage.getWidth() / IMAGE_WIDTH,
                inputImage.getHeight() / IMAGE_HEIGHT,
                inputImage.getType());

        // 生成拼图碎片，同时获得正确拖动位置
        Rectangle correctPosition = generatePuzzlePiece(inputImage, puzzlePiece);


        // 对正确拖动位置进行加密
        String positionData = String.format("%d,%d", correctPosition.x, correctPosition.y);
        String encryptedPosition=null;
        try {
            encryptedPosition = AESUtil.encrypt(positionData, encryptionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建 PuzzleResponse 对象
        PuzzleResponse puzzleResponse = new PuzzleResponse(inputImage,puzzlePiece,encryptedPosition);

        return puzzleResponse;

    }

    private Rectangle generatePuzzlePiece(BufferedImage inputImage, BufferedImage puzzlePiece) {
        Random random = new Random();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        // 指定拼图碎片的尺寸
        int pieceWidth = width / 6;
        int pieceHeight = height / 6;

        // 在原图中随机选择一个位置，作为拼图碎片的起始位置
        int x = random.nextInt(width - pieceWidth);
        int y = random.nextInt(height - pieceHeight);

        // 从原图中截取拼图碎片
        Graphics2D g = puzzlePiece.createGraphics();
        g.drawImage(inputImage, 0, 0, pieceWidth, pieceHeight, x, y, x + pieceWidth, y + pieceHeight, null);
        g.dispose();

        // 返回拼图碎片在原图中的位置
        return new Rectangle(x, y, pieceWidth, pieceHeight);

    }

    public Boolean varify(String movedPosition, String encryptedPosition) {

        // 解析用户的拖动位置
        String[] movedPositionParts = movedPosition.split(",");
        int movedX = Integer.parseInt(movedPositionParts[0]);
        int movedY = Integer.parseInt(movedPositionParts[1]);

        // 对正确拖动位置进行解密，并解析 x 和 y 坐标
        String encryptionKey = "your_secret_key";
        String decryptedPosition = null;
        try {
            decryptedPosition = AESUtil.decrypt(encryptedPosition, encryptionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] positionParts = decryptedPosition.split(",");
        int correctX = Integer.parseInt(positionParts[0]);
        int correctY = Integer.parseInt(positionParts[1]);

        // 计算用户拖动位置与正确位置之间的距离
        double distance = Math.sqrt(Math.pow((correctX - movedX), 2) + Math.pow((correctY - movedY), 2));

        // 确定允许的误差范围，并比较两个位置之间的距离是否在允许的误差范围内
        double tolerance = 5.0; // 允许的误差范围，可根据需求进行调整
        boolean success = distance <= tolerance;

        return success;
    }
}

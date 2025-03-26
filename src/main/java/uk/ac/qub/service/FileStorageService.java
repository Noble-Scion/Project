package uk.ac.qub.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR + "/profile-pictures"));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directories");
        }
    }

    public String storeProfilePicture(MultipartFile file) throws IOException {
        // Generate unique filename
        String filename = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        Path destinationPath = Paths.get(UPLOAD_DIR, "profile-pictures", filename);

        // Save the file
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // Return the URL path
        return "/profile-pictures/" + filename;
    }

    public byte[] getProfilePicture(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, "profile-pictures", fileName);
        return Files.readAllBytes(filePath);
    }

    public byte[] getPlaceholderImage(int width, int height) {
        // Create a simple colored placeholder image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Fill with a light gray background
        graphics.setColor(new Color(240, 240, 240));
        graphics.fillRect(0, 0, width, height);

        // Add some text
        graphics.setColor(Color.GRAY);
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        String text = width + "x" + height;
        FontMetrics metrics = graphics.getFontMetrics();
        int x = (width - metrics.stringWidth(text)) / 2;
        int y = (height + metrics.getHeight()) / 2;
        graphics.drawString(text, x, y);

        graphics.dispose();

        // Convert to byte array
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) return ".jpg";
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex > 0 ? filename.substring(lastDotIndex) : ".jpg";
    }

    public void deleteProfilePicture(String pictureUrl) {
        if (pictureUrl != null && !pictureUrl.startsWith("/api/placeholder")) {
            try {
                Path filePath = Paths.get(UPLOAD_DIR, pictureUrl);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
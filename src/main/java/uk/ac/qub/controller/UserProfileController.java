package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.qub.model.UserDetails;
import uk.ac.qub.model.User;
import uk.ac.qub.repository.UserDetailsRepository;
import uk.ac.qub.repository.UserRepository;
import uk.ac.qub.service.FileStorageService;
import uk.ac.qub.service.UserProfileService;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}/picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long userId) {
        try {
            UserDetails userDetails = userDetailsRepository.findByUser(
                            userRepository.findById(userId)
                                    .orElseThrow(() -> new IllegalArgumentException("User not found")))
                    .orElseThrow(() -> new IllegalArgumentException("User details not found"));

            if (userDetails.getProfilePictureUrl() != null) {
                byte[] imageData = fileStorageService.getProfilePicture(userDetails.getProfilePictureUrl());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageData);
            }

            // Return placeholder if no profile picture
            byte[] placeholderImage = fileStorageService.getPlaceholderImage(120, 120);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(placeholderImage);
        } catch (Exception e) {
            byte[] placeholderImage = fileStorageService.getPlaceholderImage(120, 120);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(placeholderImage);
        }
    }

    @PostMapping(value = "/{userId}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            String pictureUrl = userProfileService.updateProfilePicture(userId, file);
            return ResponseEntity.ok(pictureUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/placeholder/{width}/{height}")
    public ResponseEntity<byte[]> getPlaceholderImage(
            @PathVariable int width,
            @PathVariable int height) {
        byte[] imageBytes = fileStorageService.getPlaceholderImage(width, height);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}
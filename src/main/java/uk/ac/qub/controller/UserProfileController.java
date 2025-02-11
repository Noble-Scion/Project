// UserProfileController.java
package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.qub.service.UserProfileService;
import uk.ac.qub.dto.UserProfileDTO;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @PostMapping("/{userId}/bio")
    public ResponseEntity<?> updateBio(@PathVariable Long userId, @RequestBody String bio) {
        userProfileService.updateBio(userId, bio);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/profile-picture")
    public ResponseEntity<?> updateProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        String pictureUrl = userProfileService.updateProfilePicture(userId, file);
        return ResponseEntity.ok(pictureUrl);
    }
}
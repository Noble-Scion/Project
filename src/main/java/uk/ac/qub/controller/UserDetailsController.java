// UserDetailsController.java
package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.qub.service.UserDetailsService;
import uk.ac.qub.dto.CompleteProfileRequest;

@RestController
@RequestMapping("/api")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/check-profile-completion/{userId}")
    public ResponseEntity<?> checkProfileCompletion(@PathVariable Long userId) {
        try {
            boolean isComplete = userDetailsService.hasCompletedProfile(userId);
            return isComplete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        boolean isAvailable = userDetailsService.isUsernameAvailable(username);
        return isAvailable ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<?> completeProfile(@RequestBody CompleteProfileRequest request) {
        try {
            userDetailsService.saveUserDetails(request, request.getUserId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

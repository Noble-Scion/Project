package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.qub.service.EmailService;
import uk.ac.qub.service.SettingsService;
import uk.ac.qub.dto.UpdateHomeAddressRequest;
import uk.ac.qub.dto.UserActionRequest;
import uk.ac.qub.dto.ReportBugRequest;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/home-address")
    public ResponseEntity<?> updateHomeAddress(@RequestBody UpdateHomeAddressRequest request) {
        try {
            settingsService.updateHomeAddress(request.getUserId(), request.getHomeAddress());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/disable-account")
    public ResponseEntity<?> disableAccount(@RequestBody UserActionRequest request) {
        try {
            settingsService.disableAccount(request.getUserId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody UserActionRequest request) {
        try {
            settingsService.deleteAccount(request.getUserId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/report-bug")
    public ResponseEntity<?> reportBug(@RequestBody ReportBugRequest request) {
        try {
            String emailBody = String.format(
                "Bug Report from User ID: %d\n\nDescription: %s",
                request.getUserId(),
                request.getBugDescription()
            );
            
            emailService.sendEmail(
                "car.share.help1@gmail.com",
                "Bug Report",
                emailBody
            );
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


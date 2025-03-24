package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.qub.service.FriendService;
import uk.ac.qub.dto.FriendRequestBody;
import uk.ac.qub.model.Journey;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestBody request) {
        try {
            friendService.sendFriendRequest(request.getUserId(), request.getFriendUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getFriendList(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(friendService.getFriendList(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<?> getPendingRequests(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(friendService.getPendingRequests(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/journeys/{userId}")
    public ResponseEntity<?> getFriendJourneys(@PathVariable Long userId) {
        try {
            List<Journey> journeys = friendService.getFriendJourneys(userId);
            return ResponseEntity.ok(journeys);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId) {
        try {
            friendService.acceptFriendRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable Long requestId) {
        try {
            friendService.rejectFriendRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{friendshipId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long friendshipId) {
        try {
            friendService.removeFriend(friendshipId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
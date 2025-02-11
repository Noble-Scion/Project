package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.qub.model.Journey;
import uk.ac.qub.model.User;
import uk.ac.qub.repository.JourneyRepository;
import uk.ac.qub.repository.UserRepository;
import uk.ac.qub.dto.CreateJourneyRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/journeys")
public class JourneyController {

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createJourney(@RequestBody CreateJourneyRequest request) {
        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Journey journey = new Journey();
        journey.setDriver(driver);
        journey.setStartLat(request.getStartLat());
        journey.setStartLng(request.getStartLng());
        journey.setEndLat(request.getEndLat());
        journey.setEndLng(request.getEndLng());
        journey.setJourneyDateTime(request.getJourneyDateTime());
        journey.setAvailableSeats(request.getAvailableSeats());
        journey.setStartAddress(request.getStartAddress());
        journey.setEndAddress(request.getEndAddress());

        journeyRepository.save(journey);
        return ResponseEntity.ok(journey);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Journey>> getAvailableJourneys() {
        return ResponseEntity.ok(
            journeyRepository.findByStatusAndJourneyDateTimeAfterOrderByJourneyDateTime(
                "ACTIVE", 
                LocalDateTime.now()
            )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Journey>> getUserJourneys(@PathVariable Long userId) {
        return ResponseEntity.ok(
            journeyRepository.findByDriverUserIdAndStatusOrderByJourneyDateTime(
                userId, 
                "ACTIVE"
            )
        );
    }
}


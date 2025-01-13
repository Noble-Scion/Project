package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.qub.model.Journey;
import uk.ac.qub.model.User;
import uk.ac.qub.repository.JourneyRepository;
import uk.ac.qub.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

class CreateJourneyRequest {
    private Long driverId;
    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;
    private String startAddress;
    private String endAddress;
    private LocalDateTime journeyDateTime;
    private Integer availableSeats;

    // Getters and Setters
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    
    public Double getStartLat() { return startLat; }
    public void setStartLat(Double startLat) { this.startLat = startLat; }
    
    public Double getStartLng() { return startLng; }
    public void setStartLng(Double startLng) { this.startLng = startLng; }
    
    public Double getEndLat() { return endLat; }
    public void setEndLat(Double endLat) { this.endLat = endLat; }
    
    public Double getEndLng() { return endLng; }
    public void setEndLng(Double endLng) { this.endLng = endLng; }

    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }

    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }
    
    public LocalDateTime getJourneyDateTime() { return journeyDateTime; }
    public void setJourneyDateTime(LocalDateTime journeyDateTime) { this.journeyDateTime = journeyDateTime; }
    
    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
}
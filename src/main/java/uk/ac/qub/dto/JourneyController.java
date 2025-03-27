package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.qub.dto.JourneyStatsDTO;
import uk.ac.qub.model.*;
import uk.ac.qub.repository.*;
import uk.ac.qub.dto.CreateJourneyRequest;
import uk.ac.qub.service.JourneyStatsService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/journeys")
public class JourneyController {

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private JourneyPassengerRepository journeyPassengerRepository;

    @Autowired
    private JourneyStatsService journeyStatsService;

    private List<Map<String, Object>> getPassengerDetails(Journey journey) {
        return journey.getPassengers().stream()
                .map(jp -> {
                    User passenger = jp.getPassenger();
                    UserDetails userDetails = userDetailsRepository.findByUser(passenger)
                            .orElseThrow(() -> new RuntimeException("User details not found"));

                    Map<String, Object> details = new HashMap<>();
                    details.put("username", userDetails.getUsername());
                    details.put("profilePictureUrl", userDetails.getProfilePictureUrl());
                    details.put("status", jp.getStatus());
                    details.put("pickupAddress", jp.getPickupAddress());
                    details.put("joinedAt", jp.getJoinedAt());

                    return details;
                })
                .collect(Collectors.toList());
    }

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
        journey.setStatus("PENDING");

        journeyRepository.save(journey);
        return ResponseEntity.ok(journey);
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getFriendJourneys(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            List<Friend> friends = friendRepository.findByUserAndStatus(user, "ACCEPTED");
            List<Friend> friendOf = friendRepository.findByFriendAndStatus(user, "ACCEPTED");

            Set<Long> friendIds = new HashSet<>();
            friends.forEach(f -> friendIds.add(f.getFriend().getUserId()));
            friendOf.forEach(f -> friendIds.add(f.getUser().getUserId()));

            // Include both PENDING and ACTIVE journeys in the result
            List<Journey> journeys = journeyRepository.findByDriverUserIdInAndStatusInAndJourneyDateTimeAfterOrderByJourneyDateTime(
                    friendIds,
                    Arrays.asList("ACTIVE", "PENDING"),
                    LocalDateTime.now()
            );

            List<Map<String, Object>> response = journeys.stream()
                    .map(journey -> {
                        Map<String, Object> journeyMap = new HashMap<>();
                        journeyMap.put("journeyId", journey.getJourneyId());
                        journeyMap.put("startLat", journey.getStartLat());
                        journeyMap.put("startLng", journey.getStartLng());
                        journeyMap.put("endLat", journey.getEndLat());
                        journeyMap.put("endLng", journey.getEndLng());
                        journeyMap.put("startAddress", journey.getStartAddress());
                        journeyMap.put("endAddress", journey.getEndAddress());
                        journeyMap.put("journeyDateTime", journey.getJourneyDateTime());
                        journeyMap.put("availableSeats", journey.getAvailableSeats());
                        journeyMap.put("status", journey.getStatus());

                        UserDetails driverDetails = userDetailsRepository.findByUser(journey.getDriver())
                                .orElseThrow(() -> new RuntimeException("Driver details not found"));
                        Map<String, Object> driverMap = new HashMap<>();
                        driverMap.put("userId", journey.getDriver().getUserId());
                        driverMap.put("userDetails", Map.of(
                                "username", driverDetails.getUsername(),
                                "profilePictureUrl", driverDetails.getProfilePictureUrl()
                        ));
                        journeyMap.put("driver", driverMap);
                        journeyMap.put("passengers", getPassengerDetails(journey));

                        return journeyMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/check-friendship/{userId1}/{userId2}")
    public ResponseEntity<?> checkFriendshipStatus(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            User user1 = userRepository.findById(userId1)
                    .orElseThrow(() -> new IllegalArgumentException("User 1 not found"));

            User user2 = userRepository.findById(userId2)
                    .orElseThrow(() -> new IllegalArgumentException("User 2 not found"));

            boolean user1ToUser2 = friendRepository.existsByUserAndFriendAndStatus(user1, user2, "ACCEPTED");
            boolean user2ToUser1 = friendRepository.existsByUserAndFriendAndStatus(user2, user1, "ACCEPTED");

            Map<String, Object> result = new HashMap<>();
            result.put("areFriends", user1ToUser2 || user2ToUser1);
            result.put("user1ToUser2", user1ToUser2);
            result.put("user2ToUser1", user2ToUser1);

            // Get all friendship records between these users
            List<Friend> friendRecords = new ArrayList<>();
            friendRecords.addAll(friendRepository.findByUserAndFriend(user1, user2));
            friendRecords.addAll(friendRepository.findByUserAndFriend(user2, user1));

            List<Map<String, Object>> friendships = friendRecords.stream()
                    .map(f -> {
                        Map<String, Object> friendship = new HashMap<>();
                        friendship.put("id", f.getId());
                        friendship.put("from", f.getUser().getUserId());
                        friendship.put("to", f.getFriend().getUserId());
                        friendship.put("status", f.getStatus());
                        friendship.put("createdAt", f.getCreatedAt());
                        return friendship;
                    })
                    .collect(Collectors.toList());

            result.put("friendships", friendships);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/driver/{userId}")
    public ResponseEntity<List<Journey>> getDriverJourneys(@PathVariable Long userId) {
        try {
            List<Journey> journeys = journeyRepository.findByDriverUserIdOrderByJourneyDateTime(userId);
            return ResponseEntity.ok(journeys);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailableJourneys() {
        List<Journey> journeys = journeyRepository.findByStatusAndJourneyDateTimeAfterOrderByJourneyDateTime(
                "ACTIVE",
                LocalDateTime.now()
        );

        List<Map<String, Object>> response = journeys.stream()
                .map(journey -> {
                    Map<String, Object> journeyMap = new HashMap<>();
                    journeyMap.put("journeyId", journey.getJourneyId());
                    journeyMap.put("startLat", journey.getStartLat());
                    journeyMap.put("startLng", journey.getStartLng());
                    journeyMap.put("endLat", journey.getEndLat());
                    journeyMap.put("endLng", journey.getEndLng());
                    journeyMap.put("startAddress", journey.getStartAddress());
                    journeyMap.put("endAddress", journey.getEndAddress());
                    journeyMap.put("journeyDateTime", journey.getJourneyDateTime());
                    journeyMap.put("availableSeats", journey.getAvailableSeats());
                    journeyMap.put("status", journey.getStatus());
                    journeyMap.put("passengers", getPassengerDetails(journey));

                    UserDetails driverDetails = userDetailsRepository.findByUser(journey.getDriver())
                            .orElseThrow(() -> new RuntimeException("Driver details not found"));
                    Map<String, Object> driverMap = new HashMap<>();
                    driverMap.put("userId", journey.getDriver().getUserId());
                    driverMap.put("userDetails", Map.of(
                            "username", driverDetails.getUsername(),
                            "profilePictureUrl", driverDetails.getProfilePictureUrl()
                    ));
                    journeyMap.put("driver", driverMap);

                    return journeyMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<JourneyStatsDTO> getUserJourneyStats(@PathVariable Long userId) {
        try {
            JourneyStatsDTO stats = journeyStatsService.getJourneyStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserJourneys(@PathVariable Long userId) {
        List<Journey> journeys = journeyRepository.findByDriverUserIdAndStatusOrderByJourneyDateTime(
                userId,
                "ACTIVE"
        );

        List<Map<String, Object>> response = journeys.stream()
                .map(journey -> {
                    Map<String, Object> journeyMap = new HashMap<>();
                    journeyMap.put("journeyId", journey.getJourneyId());
                    journeyMap.put("startLat", journey.getStartLat());
                    journeyMap.put("startLng", journey.getStartLng());
                    journeyMap.put("endLat", journey.getEndLat());
                    journeyMap.put("endLng", journey.getEndLng());
                    journeyMap.put("startAddress", journey.getStartAddress());
                    journeyMap.put("endAddress", journey.getEndAddress());
                    journeyMap.put("journeyDateTime", journey.getJourneyDateTime());
                    journeyMap.put("availableSeats", journey.getAvailableSeats());
                    journeyMap.put("status", journey.getStatus());
                    journeyMap.put("passengers", getPassengerDetails(journey));

                    return journeyMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{journeyId}")
    public ResponseEntity<Map<String, Object>> getJourneyDetails(@PathVariable Long journeyId) {
        try {
            Journey journey = journeyRepository.findById(journeyId)
                    .orElseThrow(() -> new IllegalArgumentException("Journey not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("journeyId", journey.getJourneyId());
            response.put("startLat", journey.getStartLat());
            response.put("startLng", journey.getStartLng());
            response.put("endLat", journey.getEndLat());
            response.put("endLng", journey.getEndLng());
            response.put("startAddress", journey.getStartAddress());
            response.put("endAddress", journey.getEndAddress());
            response.put("journeyDateTime", journey.getJourneyDateTime());
            response.put("availableSeats", journey.getAvailableSeats());
            response.put("status", journey.getStatus());
            response.put("passengers", getPassengerDetails(journey));

            UserDetails driverDetails = userDetailsRepository.findByUser(journey.getDriver())
                    .orElseThrow(() -> new RuntimeException("Driver details not found"));
            Map<String, Object> driverMap = new HashMap<>();
            driverMap.put("userId", journey.getDriver().getUserId());
            driverMap.put("userDetails", Map.of(
                    "username", driverDetails.getUsername(),
                    "profilePictureUrl", driverDetails.getProfilePictureUrl()
            ));
            response.put("driver", driverMap);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{journeyId}/start")
    public ResponseEntity<?> startJourney(@PathVariable Long journeyId) {
        try {
            Journey journey = journeyRepository.findById(journeyId)
                    .orElseThrow(() -> new IllegalArgumentException("Journey not found"));

            if (!journey.getStatus().equals("PENDING")) {
                return ResponseEntity.badRequest().body("Journey is not in a valid state to start");
            }

            journey.setStatus("ACTIVE");
            journeyRepository.save(journey);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{journeyId}/complete")
    public ResponseEntity<?> completeJourney(@PathVariable Long journeyId) {
        try {
            Journey journey = journeyRepository.findById(journeyId)
                    .orElseThrow(() -> new IllegalArgumentException("Journey not found"));

            if (!journey.getStatus().equals("ACTIVE")) {
                return ResponseEntity.badRequest().body("Journey must be active to complete");
            }

            journey.setStatus("COMPLETED");
            journey.setCompletedAt(LocalDateTime.now());
            journeyRepository.save(journey);

            // Update passenger statuses
            for (JourneyPassenger passenger : journey.getPassengers()) {
                passenger.setStatus("COMPLETED");
                journeyPassengerRepository.save(passenger);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptJourney(@RequestBody JourneyAcceptRequest request) {
        try {
            Journey journey = journeyRepository.findById(request.getJourneyId())
                    .orElseThrow(() -> new IllegalArgumentException("Journey not found"));

            User passenger = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (journey.getAvailableSeats() <= 0) {
                return ResponseEntity.badRequest().body("No seats available");
            }

            if (journeyPassengerRepository.existsByJourneyAndPassengerAndStatus(journey, passenger, "ACCEPTED")) {
                return ResponseEntity.badRequest().body("Already accepted this journey");
            }

            JourneyPassenger journeyPassenger = new JourneyPassenger();
            journeyPassenger.setJourney(journey);
            journeyPassenger.setPassenger(passenger);
            journeyPassenger.setPickupLat(request.getPickupLat());
            journeyPassenger.setPickupLng(request.getPickupLng());
            journeyPassenger.setPickupAddress(request.getPickupAddress());
            journeyPassenger.setStatus("ACCEPTED");
            journeyPassenger.setJoinedAt(LocalDateTime.now());
            journeyPassengerRepository.save(journeyPassenger);

            journey.setAvailableSeats(journey.getAvailableSeats() - 1);
            journeyRepository.save(journey);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{journeyId}/cancel")
    public ResponseEntity<?> cancelJourney(@PathVariable Long journeyId) {
        try {
            Journey journey = journeyRepository.findById(journeyId)
                    .orElseThrow(() -> new IllegalArgumentException("Journey not found"));

            if (journey.getStatus().equals("COMPLETED")) {
                return ResponseEntity.badRequest().body("Cannot cancel a completed journey");
            }

            journey.setStatus("CANCELLED");
            journeyRepository.save(journey);

            // Update all passenger statuses to cancelled
            for (JourneyPassenger passenger : journey.getPassengers()) {
                passenger.setStatus("CANCELLED");
                journeyPassengerRepository.save(passenger);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{journeyId}/notify-passenger")
    public ResponseEntity<?> notifyPassenger(@PathVariable Long journeyId, @RequestBody Map<String, String> request) {
        try {
            Journey journey = journeyRepository.findById(journeyId)
                    .orElseThrow(() -> new IllegalArgumentException("Journey not found"));

            String username = request.get("username");
            UserDetails passengerDetails = userDetailsRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

            Optional<JourneyPassenger> passenger = journeyPassengerRepository.findByJourneyAndPassenger(
                    journey, passengerDetails.getUser());

            if (passenger.isPresent()) {
                JourneyPassenger jp = passenger.get();
                jp.setStatus("NOTIFIED");
                journeyPassengerRepository.save(jp);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Passenger not found for this journey");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{journeyId}/passenger/{userId}/cancel")
    public ResponseEntity<?> cancelPassengerParticipation(
            @PathVariable Long journeyId,
            @PathVariable Long userId) {
        try {
            Journey journey = journeyRepository.findById(journeyId)
                    .orElseThrow(() -> new IllegalArgumentException("Journey not found"));

            User passenger = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Optional<JourneyPassenger> journeyPassenger = journeyPassengerRepository
                    .findByJourneyAndPassenger(journey, passenger);

            if (journeyPassenger.isPresent()) {
                if (journey.getStatus().equals("COMPLETED")) {
                    return ResponseEntity.badRequest().body("Cannot cancel participation in a completed journey");
                }

                // Update passenger status
                JourneyPassenger jp = journeyPassenger.get();
                jp.setStatus("CANCELLED");
                journeyPassengerRepository.save(jp);

                // Update available seats
                journey.setAvailableSeats(journey.getAvailableSeats() + 1);
                journeyRepository.save(journey);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Passenger not found for this journey");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    static class JourneyAcceptRequest {
        private Long journeyId;
        private Long userId;
        private Double pickupLat;
        private Double pickupLng;
        private String pickupAddress;

        // Getters and setters
        public Long getJourneyId() { return journeyId; }
        public void setJourneyId(Long journeyId) { this.journeyId = journeyId; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Double getPickupLat() { return pickupLat; }
        public void setPickupLat(Double pickupLat) { this.pickupLat = pickupLat; }

        public Double getPickupLng() { return pickupLng; }
        public void setPickupLng(Double pickupLng) { this.pickupLng = pickupLng; }

        public String getPickupAddress() { return pickupAddress; }
        public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    }
}
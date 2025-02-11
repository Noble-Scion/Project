// UserProfileService.java
package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.qub.dto.UserProfileDTO;
import uk.ac.qub.dto.JourneyDTO;
import uk.ac.qub.model.User;
import uk.ac.qub.model.UserDetails;
import uk.ac.qub.model.Journey;
import uk.ac.qub.repository.UserRepository;
import uk.ac.qub.repository.UserDetailsRepository;
import uk.ac.qub.repository.JourneyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private JourneyRepository journeyRepository;

    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetails userDetails = userDetailsRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("User details not found"));

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setUsername(userDetails.getUsername());
        profileDTO.setFullName(userDetails.getFullName());
        profileDTO.setBio(userDetails.getBio());
        profileDTO.setProfilePictureUrl(userDetails.getProfilePictureUrl());

        // Get upcoming journeys (both as driver and passenger)
        List<Journey> driverJourneys = journeyRepository.findByDriverUserIdAndStatusOrderByJourneyDateTime(
                userId, "ACTIVE");

        // Convert journeys to DTOs
        List<JourneyDTO> upcomingJourneys = driverJourneys.stream()
                .map(journey -> {
                    JourneyDTO dto = new JourneyDTO();
                    dto.setJourneyId(journey.getJourneyId());
                    dto.setStartAddress(journey.getStartAddress());
                    dto.setEndAddress(journey.getEndAddress());
                    dto.setJourneyDateTime(journey.getJourneyDateTime());
                    dto.setDriver(true);
                    dto.setAvailableSeats(journey.getAvailableSeats());
                    return dto;
                })
                .collect(Collectors.toList());

        profileDTO.setUpcomingJourneys(upcomingJourneys);

        // Set statistics (placeholder values for now)
        profileDTO.setJourneysTaken(0);
        profileDTO.setJourneysMade(driverJourneys.size());
        profileDTO.setCarbonOffset(0.0);

        return profileDTO;
    }

    public void updateBio(Long userId, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetails userDetails = userDetailsRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("User details not found"));

        userDetails.setBio(bio);
        userDetailsRepository.save(userDetails);
    }

    public String updateProfilePicture(Long userId, MultipartFile file) {
        // TODO: Implement file storage logic
        // For now, we'll just store a placeholder URL
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetails userDetails = userDetailsRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("User details not found"));

        String pictureUrl = "/api/placeholder/120/120"; // Placeholder URL
        userDetails.setProfilePictureUrl(pictureUrl);
        userDetailsRepository.save(userDetails);

        return pictureUrl;
    }
}
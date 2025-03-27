package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.qub.dto.JourneyStatsDTO;
import uk.ac.qub.model.User;
import uk.ac.qub.repository.JourneyRepository;
import uk.ac.qub.repository.JourneyPassengerRepository;
import uk.ac.qub.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class JourneyStatsService {
    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JourneyPassengerRepository journeyPassengerRepository;

    public JourneyStatsDTO getJourneyStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get journeys as driver
        int journeysMade = (int) journeyRepository.countByDriverUserIdAndStatus(userId, "COMPLETED");

        // Get journeys as passenger
        int journeysTaken = (int) journeyPassengerRepository.countByPassengerAndJourneyStatus(user, "COMPLETED");

        // Get upcoming journeys (both as driver and passenger)
        int upcomingJourneys = (int) (
                // Count active journeys where user is driver
                journeyRepository.countByDriverUserIdAndStatusAndJourneyDateTimeAfter(
                        userId,
                        "ACTIVE",
                        LocalDateTime.now()
                ) +
                        // Count active journeys where user is passenger
                        journeyPassengerRepository.countByPassengerAndJourneyStatusAndJourneyJourneyDateTimeAfter(
                                user,
                                "ACTIVE",
                                LocalDateTime.now()
                        )
        );

        int cancelledJourneys = (int) (
                journeyRepository.countByDriverUserIdAndStatus(userId, "CANCELLED") +
                        journeyPassengerRepository.countByPassengerAndJourneyStatus(user, "CANCELLED")
        );

        return new JourneyStatsDTO(journeysMade, journeysTaken, upcomingJourneys, cancelledJourneys);
    }
}
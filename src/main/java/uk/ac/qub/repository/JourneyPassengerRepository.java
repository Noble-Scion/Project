package uk.ac.qub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.qub.model.Journey;
import uk.ac.qub.model.JourneyPassenger;
import uk.ac.qub.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JourneyPassengerRepository extends JpaRepository<JourneyPassenger, Long> {
    boolean existsByJourneyAndPassengerAndStatus(Journey journey, User passenger, String status);

    Optional<JourneyPassenger> findByJourneyAndPassenger(Journey journey, User passenger);

    long countByPassengerAndJourneyStatus(User passenger, String journeyStatus);

    List<JourneyPassenger> findByPassengerUserIdAndJourneyStatus(Long userId, String status);

    long countByPassengerAndJourneyStatusAndJourneyJourneyDateTimeAfter(
            User passenger,
            String status,
            LocalDateTime dateTime
    );
}
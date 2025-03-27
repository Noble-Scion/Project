package uk.ac.qub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.qub.model.Journey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    List<Journey> findByDriverUserIdInAndStatusInAndJourneyDateTimeAfterOrderByJourneyDateTime(
            Set<Long> driverIds,
            List<String> statuses,
            LocalDateTime dateTime
    );

    List<Journey> findByDriverUserIdInAndStatusAndJourneyDateTimeAfterOrderByJourneyDateTime(
            Set<Long> driverIds,
            String status,
            LocalDateTime dateTime
    );

    List<Journey> findByStatusAndJourneyDateTimeAfterOrderByJourneyDateTime(
            String status,
            LocalDateTime dateTime
    );

    List<Journey> findByDriverUserIdAndStatusOrderByJourneyDateTime(
            Long userId,
            String status
    );

    long countByDriverUserIdAndStatus(Long userId, String status);

    List<Journey> findByDriverUserIdAndStatus(Long userId, String status);

    List<Journey> findByDriverUserIdOrderByJourneyDateTime(Long userId);

    long countByDriverUserIdAndStatusAndJourneyDateTimeAfter(
            Long userId,
            String status,
            LocalDateTime dateTime
    );
}
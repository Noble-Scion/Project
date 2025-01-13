package uk.ac.qub.repository;

import uk.ac.qub.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    List<Journey> findByStatusAndJourneyDateTimeAfterOrderByJourneyDateTime(String status, LocalDateTime dateTime);
    List<Journey> findByDriverUserIdAndStatusOrderByJourneyDateTime(Long userId, String status);
}
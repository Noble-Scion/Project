// UserDetailsRepository.java
package uk.ac.qub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.qub.model.UserDetails;
import uk.ac.qub.model.User;
import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    boolean existsByUsername(String username);
    Optional<UserDetails> findByUsername(String username);
    Optional<UserDetails> findByUser(User user);
    boolean existsByUser(User user);
}

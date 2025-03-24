package uk.ac.qub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.qub.model.Friend;
import uk.ac.qub.model.User;
import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUserAndStatus(User user, String status);
    List<Friend> findByFriendAndStatus(User friend, String status);
    boolean existsByUserAndFriendAndStatus(User user, User friend, String status);
    List<Friend> findByUserAndFriend(User user, User friend);
}
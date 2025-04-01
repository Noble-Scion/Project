// FriendService.java
package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.qub.model.Friend;
import uk.ac.qub.model.User;
import uk.ac.qub.model.UserDetails;
import uk.ac.qub.model.Journey;
import uk.ac.qub.repository.FriendRepository;
import uk.ac.qub.repository.UserDetailsRepository;
import uk.ac.qub.repository.UserRepository;
import uk.ac.qub.repository.JourneyRepository;
import uk.ac.qub.dto.FriendDTO;
import uk.ac.qub.dto.FriendRequestDTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private JourneyRepository journeyRepository;

    public void sendFriendRequest(Long userId, String friendUsername) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        UserDetails friendDetails = userDetailsRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new Exception("User not found with username: " + friendUsername));

        User friend = friendDetails.getUser();

        if (friendRepository.existsByUserAndFriendAndStatus(user, friend, "ACCEPTED") ||
                friendRepository.existsByUserAndFriendAndStatus(friend, user, "ACCEPTED")) {
            throw new Exception("Already friends");
        }

        Friend friendRequest = new Friend();
        friendRequest.setUser(user);
        friendRequest.setFriend(friend);
        friendRepository.save(friendRequest);
    }

    public List<FriendDTO> getFriendList(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Friend> sentFriends = friendRepository.findByUserAndStatus(user, "ACCEPTED");
        List<Friend> receivedFriends = friendRepository.findByFriendAndStatus(user, "ACCEPTED");

        List<FriendDTO> friendsList = new ArrayList<>();

        for (Friend friend : sentFriends) {
            UserDetails friendDetails = userDetailsRepository.findByUser(friend.getFriend())
                    .orElseThrow(() -> new Exception("Friend details not found"));
            friendsList.add(new FriendDTO(
                    friend.getFriend().getUserId(),
                    friendDetails.getUsername(),
                    friend.getId()
            ));
        }

        for (Friend friend : receivedFriends) {
            UserDetails friendDetails = userDetailsRepository.findByUser(friend.getUser())
                    .orElseThrow(() -> new Exception("Friend details not found"));
            friendsList.add(new FriendDTO(
                    friend.getUser().getUserId(),
                    friendDetails.getUsername(),
                    friend.getId()
            ));
        }

        return friendsList;
    }

    public List<Journey> getFriendJourneys(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Friend> sentFriends = friendRepository.findByUserAndStatus(user, "ACCEPTED");
        List<Friend> receivedFriends = friendRepository.findByFriendAndStatus(user, "ACCEPTED");

        Set<Long> friendIds = new HashSet<>();

        // Add IDs from sent friendships
        sentFriends.forEach(f -> friendIds.add(f.getFriend().getUserId()));

        // Add IDs from received friendships
        receivedFriends.forEach(f -> friendIds.add(f.getUser().getUserId()));

        // If no friends, return empty list
        if (friendIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Include both PENDING and ACTIVE journeys
        return journeyRepository.findByDriverUserIdInAndStatusInAndJourneyDateTimeAfterOrderByJourneyDateTime(
                friendIds,
                Arrays.asList("ACTIVE", "PENDING"),
                LocalDateTime.now()
        );
    }

    public List<FriendRequestDTO> getPendingRequests(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        return friendRepository.findByFriendAndStatus(user, "PENDING")
                .stream()
                .map(friend -> {
                    UserDetails requesterDetails = userDetailsRepository.findByUser(friend.getUser())
                            .orElseThrow(() -> new RuntimeException("Friend details not found"));
                    return new FriendRequestDTO(
                            friend.getId(),
                            friend.getUser().getUserId(),
                            requesterDetails.getUsername()
                    );
                })
                .collect(Collectors.toList());
    }

    public void acceptFriendRequest(Long requestId) throws Exception {
        Friend friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new Exception("Friend request not found"));
        friend.setStatus("ACCEPTED");
        friendRepository.save(friend);
    }

    public void rejectFriendRequest(Long requestId) throws Exception {
        Friend friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new Exception("Friend request not found"));
        friend.setStatus("REJECTED");
        friendRepository.save(friend);
    }

    public void removeFriend(Long friendshipId) throws Exception {
        friendRepository.deleteById(friendshipId);
    }
}
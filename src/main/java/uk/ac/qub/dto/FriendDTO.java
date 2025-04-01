// FriendDTO.java
package uk.ac.qub.dto;

public class FriendDTO {
    private Long userId;
    private String username;
    private Long friendshipId;

    public FriendDTO(Long userId, String username, Long friendshipId) {
        this.userId = userId;
        this.username = username;
        this.friendshipId = friendshipId;
    }

    // Getters
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public Long getFriendshipId() { return friendshipId; }
}
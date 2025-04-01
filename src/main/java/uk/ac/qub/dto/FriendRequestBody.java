// FriendRequestBody.java
package uk.ac.qub.dto;

public class FriendRequestBody {
    private Long userId;
    private String friendUsername;

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFriendUsername() { return friendUsername; }
    public void setFriendUsername(String friendUsername) { this.friendUsername = friendUsername; }
}
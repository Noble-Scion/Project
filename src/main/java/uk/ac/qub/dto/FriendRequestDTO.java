// FriendRequestDTO.java
package uk.ac.qub.dto;

public class FriendRequestDTO {
    private Long requestId;
    private Long userId;
    private String username;

    public FriendRequestDTO(Long requestId, Long userId, String username) {
        this.requestId = requestId;
        this.userId = userId;
        this.username = username;
    }

    // Getters
    public Long getRequestId() { return requestId; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
}
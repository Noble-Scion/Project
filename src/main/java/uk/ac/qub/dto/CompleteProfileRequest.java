package uk.ac.qub.dto;

public class CompleteProfileRequest {
    private String username;
    private String fullName;
    private Long userId;
    private CarDTO car;

    // Previous getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    // New car getters and setters
    public CarDTO getCar() { return car; }
    public void setCar(CarDTO car) { this.car = car; }
}
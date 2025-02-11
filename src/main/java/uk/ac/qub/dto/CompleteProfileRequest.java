// CompleteProfileRequest.java
package uk.ac.qub.dto;

public class CompleteProfileRequest {
    private String username;
    private String fullName;  // Added this field
    private CarDetails carDetails;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }  // New getter
    public void setFullName(String fullName) { this.fullName = fullName; }  // New setter
    public CarDetails getCarDetails() { return carDetails; }
    public void setCarDetails(CarDetails carDetails) { this.carDetails = carDetails; }

    public static class CarDetails {
        // Existing car details remain the same
        private String registration;
        private String make;
        private String model;
        private Integer year;
        private Double averageMpg;

        // Existing getters and setters remain the same
        public String getRegistration() { return registration; }
        public void setRegistration(String registration) { this.registration = registration; }
        public String getMake() { return make; }
        public void setMake(String make) { this.make = make; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }
        public Double getAverageMpg() { return averageMpg; }
        public void setAverageMpg(Double averageMpg) { this.averageMpg = averageMpg; }
    }

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
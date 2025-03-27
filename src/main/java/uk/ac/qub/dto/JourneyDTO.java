package uk.ac.qub.dto;

import java.time.LocalDateTime;
import java.util.List;

public class JourneyDTO {
    private Long journeyId;
    private String driverName;
    private String driverProfilePicture;
    private String startAddress;
    private String endAddress;
    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;
    private String pickupAddress;
    private LocalDateTime journeyDateTime;
    private boolean isDriver;
    private int availableSeats;
    private int passengerCount;
    private List<PassengerDTO> passengers;
    private String status;

    public static class PassengerDTO {
        private Long userId;
        private String username;
        private String profilePictureUrl;
        private String pickupAddress;
        private Double pickupLat;
        private Double pickupLng;
        private String status;
        private LocalDateTime joinedAt;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getProfilePictureUrl() { return profilePictureUrl; }
        public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

        public String getPickupAddress() { return pickupAddress; }
        public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

        public Double getPickupLat() { return pickupLat; }
        public void setPickupLat(Double pickupLat) { this.pickupLat = pickupLat; }

        public Double getPickupLng() { return pickupLng; }
        public void setPickupLng(Double pickupLng) { this.pickupLng = pickupLng; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDateTime getJoinedAt() { return joinedAt; }
        public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    }

    // Getters and setters
    public Long getJourneyId() { return journeyId; }
    public void setJourneyId(Long journeyId) { this.journeyId = journeyId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverProfilePicture() { return driverProfilePicture; }
    public void setDriverProfilePicture(String driverProfilePicture) { this.driverProfilePicture = driverProfilePicture; }

    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }

    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }

    public Double getStartLat() { return startLat; }
    public void setStartLat(Double startLat) { this.startLat = startLat; }

    public Double getStartLng() { return startLng; }
    public void setStartLng(Double startLng) { this.startLng = startLng; }

    public Double getEndLat() { return endLat; }
    public void setEndLat(Double endLat) { this.endLat = endLat; }

    public Double getEndLng() { return endLng; }
    public void setEndLng(Double endLng) { this.endLng = endLng; }

    public LocalDateTime getJourneyDateTime() { return journeyDateTime; }
    public void setJourneyDateTime(LocalDateTime journeyDateTime) { this.journeyDateTime = journeyDateTime; }

    public boolean isDriver() { return isDriver; }
    public void setDriver(boolean driver) { isDriver = driver; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }

    public List<PassengerDTO> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerDTO> passengers) { this.passengers = passengers; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
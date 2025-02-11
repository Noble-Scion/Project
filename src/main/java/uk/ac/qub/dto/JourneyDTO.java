// JourneyDTO.java
package uk.ac.qub.dto;

import java.time.LocalDateTime;

public class JourneyDTO {
    private Long journeyId;
    private String driverName;
    private String startAddress;
    private String endAddress;
    private LocalDateTime journeyDateTime;
    private boolean isDriver;
    private int availableSeats;

    // Getters and setters
    public Long getJourneyId() { return journeyId; }
    public void setJourneyId(Long journeyId) { this.journeyId = journeyId; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }
    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }
    public LocalDateTime getJourneyDateTime() { return journeyDateTime; }
    public void setJourneyDateTime(LocalDateTime journeyDateTime) { this.journeyDateTime = journeyDateTime; }
    public boolean isDriver() { return isDriver; }
    public void setDriver(boolean driver) { isDriver = driver; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}




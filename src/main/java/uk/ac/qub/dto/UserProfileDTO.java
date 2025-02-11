// UserProfileDTO.java
package uk.ac.qub.dto;

import java.util.List;

public class UserProfileDTO {
    private String username;
    private String fullName;
    private String bio;
    private String profilePictureUrl;
    private int journeysTaken;
    private int journeysMade;
    private double carbonOffset;
    private List<JourneyDTO> upcomingJourneys;

    // Constructor
    public UserProfileDTO() {}

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public int getJourneysTaken() { return journeysTaken; }
    public void setJourneysTaken(int journeysTaken) { this.journeysTaken = journeysTaken; }
    public int getJourneysMade() { return journeysMade; }
    public void setJourneysMade(int journeysMade) { this.journeysMade = journeysMade; }
    public double getCarbonOffset() { return carbonOffset; }
    public void setCarbonOffset(double carbonOffset) { this.carbonOffset = carbonOffset; }
    public List<JourneyDTO> getUpcomingJourneys() { return upcomingJourneys; }
    public void setUpcomingJourneys(List<JourneyDTO> upcomingJourneys) { this.upcomingJourneys = upcomingJourneys; }
}
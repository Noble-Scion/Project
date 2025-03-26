package uk.ac.qub.dto;

import uk.ac.qub.model.Car;
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
    private Car car;
    private double totalCarbonOffset;
    private double monthlyCarbon;

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

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public double getTotalCarbonOffset() { return totalCarbonOffset; }
    public void setTotalCarbonOffset(double totalCarbonOffset) { this.totalCarbonOffset = totalCarbonOffset; }

    public double getMonthlyCarbon() { return monthlyCarbon; }
    public void setMonthlyCarbon(double monthlyCarbon) { this.monthlyCarbon = monthlyCarbon; }

    private String selectedRewardBanner;

    public String getSelectedRewardBanner() { return selectedRewardBanner; }
    public void setSelectedRewardBanner(String selectedRewardBanner) { this.selectedRewardBanner = selectedRewardBanner; }
}
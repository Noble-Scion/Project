package uk.ac.qub.dto;

public class JourneyStatsDTO {
    private int journeysMade;
    private int journeysTaken;
    private int upcomingJourneys;
    private int cancelledJourneys;

    public JourneyStatsDTO(int journeysMade, int journeysTaken, int upcomingJourneys, int cancelledJourneys) {
        this.journeysMade = journeysMade;
        this.journeysTaken = journeysTaken;
        this.upcomingJourneys = upcomingJourneys;
        this.cancelledJourneys = cancelledJourneys;
    }

    // Getters and setters
    public int getJourneysMade() { return journeysMade; }
    public void setJourneysMade(int journeysMade) { this.journeysMade = journeysMade; }

    public int getJourneysTaken() { return journeysTaken; }
    public void setJourneysTaken(int journeysTaken) { this.journeysTaken = journeysTaken; }

    public int getUpcomingJourneys() { return upcomingJourneys; }
    public void setUpcomingJourneys(int upcomingJourneys) { this.upcomingJourneys = upcomingJourneys; }

    public int getCancelledJourneys() { return cancelledJourneys; }
    public void setCancelledJourneys(int cancelledJourneys) { this.cancelledJourneys = cancelledJourneys; }
}
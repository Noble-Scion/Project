package uk.ac.qub.model;

public enum JourneyStatus {
    PENDING,    // Journey created but not started
    ACTIVE,     // Journey is in progress
    COMPLETED,  // Journey has finished
    CANCELLED   // Journey was cancelled
}
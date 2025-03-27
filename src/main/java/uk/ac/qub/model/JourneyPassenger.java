package uk.ac.qub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "journey_passengers")
public class JourneyPassenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = false)
    private Journey journey;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    @Column(nullable = false)
    private Double pickupLat;

    @Column(nullable = false)
    private Double pickupLng;

    @Column(length = 255)
    private String pickupAddress;

    @Column(nullable = false)
    private String status = "ACCEPTED"; // ACCEPTED, DRIVER_ARRIVED, PICKED_UP, COMPLETED, CANCELLED

    @Column(nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Column
    private LocalDateTime driverArrivedAt;

    @Column
    private LocalDateTime pickedUpAt;

    @Column
    private LocalDateTime completedAt;

    @Column
    private LocalDateTime cancelledAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Journey getJourney() { return journey; }
    public void setJourney(Journey journey) { this.journey = journey; }

    public User getPassenger() { return passenger; }
    public void setPassenger(User passenger) { this.passenger = passenger; }

    public Double getPickupLat() { return pickupLat; }
    public void setPickupLat(Double pickupLat) { this.pickupLat = pickupLat; }

    public Double getPickupLng() { return pickupLng; }
    public void setPickupLng(Double pickupLng) { this.pickupLng = pickupLng; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        // Update corresponding timestamp based on status
        switch(status) {
            case "DRIVER_ARRIVED":
                this.driverArrivedAt = LocalDateTime.now();
                break;
            case "PICKED_UP":
                this.pickedUpAt = LocalDateTime.now();
                break;
            case "COMPLETED":
                this.completedAt = LocalDateTime.now();
                break;
            case "CANCELLED":
                this.cancelledAt = LocalDateTime.now();
                break;
        }
    }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    public LocalDateTime getDriverArrivedAt() { return driverArrivedAt; }
    public void setDriverArrivedAt(LocalDateTime driverArrivedAt) { this.driverArrivedAt = driverArrivedAt; }

    public LocalDateTime getPickedUpAt() { return pickedUpAt; }
    public void setPickedUpAt(LocalDateTime pickedUpAt) { this.pickedUpAt = pickedUpAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
}
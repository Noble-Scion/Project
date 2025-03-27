package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.qub.model.*;
import uk.ac.qub.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CarbonOffsetService {

    private static final Map<String, Double> FUEL_CO2_PER_GALLON = Map.of(
            "PETROL", 19.6,  // In kg CO2 per gallon
            "DIESEL", 22.4,
            "HYBRID", 19.6,
            "ELECTRIC", 0.0
    );

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private JourneyPassengerRepository journeyPassengerRepository;

    public double calculateJourneyCarbonOffset(Journey journey, Car car) {
        if (car == null || journey == null) return 0.0;

        // Calculate journey distance in miles (you'll need to implement this based on your coordinates)
        double journeyDistance = calculateDistance(
                journey.getStartLat(), journey.getStartLng(),
                journey.getEndLat(), journey.getEndLng()
        );

        // Calculate base carbon emissions for the journey
        double gallonsUsed = journeyDistance / car.getAvgMpg();
        double co2Emissions = gallonsUsed * FUEL_CO2_PER_GALLON.getOrDefault(car.getFuelType(), 0.0);

        // Calculate passenger impact
        int totalPassengers = journey.getPassengers().size();
        if (totalPassengers == 0) return 0.0;

        double totalOffset = 0.0;

        for (JourneyPassenger passenger : journey.getPassengers()) {
            // Calculate what portion of the journey this passenger was present for
            double passengerDistance = calculateDistance(
                    passenger.getPickupLat(), passenger.getPickupLng(),
                    journey.getEndLat(), journey.getEndLng()
            );

            double passengerPortion = passengerDistance / journeyDistance;
            double passengerOffset = (co2Emissions * passengerPortion) / (totalPassengers + 1);
            totalOffset += passengerOffset;
        }

        return totalOffset;
    }

    public double getUserTotalCarbonOffset(Long userId) {
        double totalOffset = 0.0;

        // Get user's completed journeys as driver
        Car userCar = carRepository.findByUserUserId(userId);
        if (userCar != null) {
            List<Journey> driverJourneys = journeyRepository
                    .findByDriverUserIdAndStatus(userId, "COMPLETED");

            for (Journey journey : driverJourneys) {
                totalOffset += calculateJourneyCarbonOffset(journey, userCar);
            }
        }

        // Get user's completed journeys as passenger
        List<JourneyPassenger> passengerJourneys = journeyPassengerRepository
                .findByPassengerUserIdAndJourneyStatus(userId, "COMPLETED");

        for (JourneyPassenger jp : passengerJourneys) {
            Journey journey = jp.getJourney();
            Car driverCar = carRepository.findByUserUserId(journey.getDriver().getUserId());
            if (driverCar != null) {
                // Calculate passenger's portion of the offset
                double journeyOffset = calculateJourneyCarbonOffset(journey, driverCar);
                double passengerDistance = calculateDistance(
                        jp.getPickupLat(), jp.getPickupLng(),
                        journey.getEndLat(), journey.getEndLng()
                );
                double journeyDistance = calculateDistance(
                        journey.getStartLat(), journey.getStartLng(),
                        journey.getEndLat(), journey.getEndLng()
                );
                totalOffset += (journeyOffset * passengerDistance / journeyDistance);
            }
        }

        return totalOffset;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula for calculating distance between two points
        final int R = 3959; // Earth's radius in miles

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
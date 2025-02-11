// CarService.java
package uk.ac.qub.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.ac.qub.dto.CompleteProfileRequest.CarDetails;

@Service
public class CarService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String VEHICLE_API_URL = "https://driver-vehicle-licensing.api.gov.uk/vehicle-enquiry/v1/vehicles";

    public CarDetails lookupCarDetails(String registration) {
        // This is a mock implementation since we don't have actual API access
        // In production, you would call the DVA API

        // Validate registration format
        if (!isValidRegistration(registration)) {
            throw new IllegalArgumentException("Invalid registration format");
        }

        // Mock response
        CarDetails details = new CarDetails();
        details.setRegistration(registration);
        details.setMake("Toyota"); // Mock data
        details.setModel("Corolla"); // Mock data
        details.setYear(2020); // Mock data
        details.setAverageMpg(50.5); // Mock data

        return details;
    }

    private boolean isValidRegistration(String registration) {
        // Basic UK registration format validation
        // This is a simplified version - actual validation would be more complex
        return registration.matches("[A-Z]{2}\\d{2}\\s?[A-Z]{3}");
    }
}
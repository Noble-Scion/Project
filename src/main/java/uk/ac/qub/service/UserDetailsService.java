package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.qub.dto.CompleteProfileRequest;
import uk.ac.qub.dto.CarDTO;
import uk.ac.qub.model.User;
import uk.ac.qub.model.UserDetails;
import uk.ac.qub.model.Car;
import uk.ac.qub.repository.UserDetailsRepository;
import uk.ac.qub.repository.UserRepository;
import uk.ac.qub.repository.CarRepository;

@Service
@Transactional
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    public boolean hasCompletedProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userDetailsRepository.existsByUser(user);
    }

    public boolean isUsernameAvailable(String username) {
        return !userDetailsRepository.existsByUsername(username);
    }

    @Transactional
    public void saveUserDetails(CompleteProfileRequest request, Long userId) throws Exception {
        // Check if user already has details
        if (hasCompletedProfile(userId)) {
            throw new Exception("Profile already completed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Validate username availability
        if (!isUsernameAvailable(request.getUsername())) {
            throw new Exception("Username already taken");
        }

        // Save user details
        UserDetails userDetails = new UserDetails();
        userDetails.setUser(user);
        userDetails.setUsername(request.getUsername());
        userDetails.setFullName(request.getFullName());
        userDetailsRepository.save(userDetails);

        // Save car details if provided
        if (request.getCar() != null) {
            Car car = new Car();
            car.setUser(user);
            car.setRegistrationNumber(request.getCar().getRegistrationNumber());
            car.setMake(request.getCar().getMake());
            car.setModel(request.getCar().getModel());
            car.setYear(request.getCar().getYear());
            car.setFuelType(request.getCar().getFuelType());
            car.setAvgMpg(request.getCar().getAvgMpg());
            carRepository.save(car);
        }
    }

    @Transactional
    public void updateCarDetails(Long userId, CarDTO carDTO) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Car car = carRepository.findByUserUserId(userId);
        if (car == null) {
            car = new Car();
            car.setUser(user);
        }

        car.setRegistrationNumber(carDTO.getRegistrationNumber());
        car.setMake(carDTO.getMake());
        car.setModel(carDTO.getModel());
        car.setYear(carDTO.getYear());
        car.setFuelType(carDTO.getFuelType());
        car.setAvgMpg(carDTO.getAvgMpg());

        carRepository.save(car);
    }
}
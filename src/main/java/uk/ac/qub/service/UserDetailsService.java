// UserDetailsService.java
package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.qub.dto.CompleteProfileRequest;
import uk.ac.qub.model.Car;
import uk.ac.qub.model.User;
import uk.ac.qub.model.UserDetails;
import uk.ac.qub.repository.CarRepository;
import uk.ac.qub.repository.UserDetailsRepository;
import uk.ac.qub.repository.UserRepository;

@Service
@Transactional
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean hasCompletedProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return userDetailsRepository.existsByUser(user);
    }

    public boolean isUsernameAvailable(String username) {
        return !userDetailsRepository.existsByUsername(username);
    }

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

        // Save car details
        Car car = new Car();
        car.setUser(user);
        car.setRegistrationNumber(request.getCarDetails().getRegistration());
        car.setMake(request.getCarDetails().getMake());
        car.setModel(request.getCarDetails().getModel());
        car.setYear(request.getCarDetails().getYear());
        car.setAverageMpg(request.getCarDetails().getAverageMpg());
        carRepository.save(car);
    }
}

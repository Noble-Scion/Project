package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.qub.model.User;
import uk.ac.qub.repository.UserRepository;

@Service
@Transactional
public class SettingsService {

    @Autowired
    private UserRepository userRepository;

    public void updateHomeAddress(Long userId, String homeAddress) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setHomeAddress(homeAddress);
        userRepository.save(user);
    }

    public void disableAccount(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setAccountStatus("DISABLED");
        userRepository.save(user);
    }

    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // Soft delete - mark as deleted but keep the record
        user.setAccountStatus("DELETED");
        userRepository.save(user);
        
        // For hard delete, uncomment the following line:
        // userRepository.deleteById(userId);
    }
}
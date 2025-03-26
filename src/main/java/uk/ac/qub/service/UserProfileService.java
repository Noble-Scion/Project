package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.qub.model.User;
import uk.ac.qub.model.UserDetails;
import uk.ac.qub.repository.UserDetailsRepository;
import uk.ac.qub.repository.UserRepository;

import java.io.IOException;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public String updateProfilePicture(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetails userDetails = userDetailsRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("User details not found"));

        // Delete old profile picture if exists
        if (userDetails.getProfilePictureUrl() != null) {
            fileStorageService.deleteProfilePicture(userDetails.getProfilePictureUrl());
        }

        // Store new picture
        String pictureUrl = fileStorageService.storeProfilePicture(file);
        userDetails.setProfilePictureUrl(pictureUrl);
        userDetailsRepository.save(userDetails);

        return pictureUrl;
    }
}
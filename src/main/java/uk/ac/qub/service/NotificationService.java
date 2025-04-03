package uk.ac.qub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.qub.model.User;
import uk.ac.qub.model.Notification;
import uk.ac.qub.repository.NotificationRepository;
import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    public void createNotification(User user, String type, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);

        // Send email for important notifications
        if (isImportantNotification(type)) {
            emailService.sendEmail(
                    user.getEmail(),
                    "CarShare Journey Update",
                    message
            );
        }
    }

    private boolean isImportantNotification(String type) {
        return type.equals("DRIVER_ARRIVED") ||
                type.equals("JOURNEY_CANCELLED") ||
                type.equals("JOURNEY_STARTING_SOON");
    }

    public void createDriverArrivedNotification(User passenger, String driverName, String pickupAddress) {
        String message = String.format(
                "Your driver %s has arrived at %s",
                driverName,
                pickupAddress
        );
        createNotification(passenger, "DRIVER_ARRIVED", message);
    }

    public void createJourneyCancelledNotification(User passenger, String driverName, String reason) {
        String message = String.format(
                "Your journey with %s has been cancelled. Reason: %s",
                driverName,
                reason
        );
        createNotification(passenger, "JOURNEY_CANCELLED", message);
    }

    public void createJourneyStartedNotification(User passenger, String driverName) {
        String message = String.format(
                "Your journey with %s has started",
                driverName
        );
        createNotification(passenger, "JOURNEY_STARTED", message);
    }

    public void createJourneyCompletedNotification(User passenger, String driverName) {
        String message = String.format(
                "Your journey with %s has been completed",
                driverName
        );
        createNotification(passenger, "JOURNEY_COMPLETED", message);
    }

    public void createPassengerCancelledNotification(User driver, String passengerName) {
        String message = String.format(
                "Passenger %s has cancelled their participation in your journey",
                passengerName
        );
        createNotification(driver, "PASSENGER_CANCELLED", message);
    }

    public void createJourneyStartingSoonNotification(User passenger, String driverName, String time) {
        String message = String.format(
                "Your journey with %s starts in one hour at %s",
                driverName,
                time
        );
        createNotification(passenger, "JOURNEY_STARTING_SOON", message);
    }
}
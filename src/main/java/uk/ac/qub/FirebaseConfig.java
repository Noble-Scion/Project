package uk.ac.qub;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class FirebaseConfig {

    private final Environment environment;

    @Autowired
    public FirebaseConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public void initialize() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(environment.getProperty("firebase.service.account.key"));

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}

}

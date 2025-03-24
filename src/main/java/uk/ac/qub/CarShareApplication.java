package uk.ac.qub;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uk.ac.qub.service.RewardService;

@EnableJpaRepositories(basePackages = "uk.ac.qub.repository")
@SpringBootApplication
public class CarShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarShareApplication.class, args);
    }

    @Autowired
    private RewardService rewardService;

    @PostConstruct
    public void init() {
        rewardService.initializeDefaultRewards();
    }
}

package uk.ac.qub;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    private Date dateOfBirth;


    // getters and setters
    public Long geUserId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String[] getAccountInfo() {
        return new String[]{String.valueOf(id), username, name, String.valueOf(dateOfBirth)};
    }
}
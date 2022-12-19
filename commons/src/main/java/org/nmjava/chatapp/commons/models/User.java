package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID userID;
    private String username;
    private String password;
    private String fullName;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private Boolean online;
    private Boolean activated;
    private LocalDateTime createAt;
//    private BooleanProperty active = new SimpleBooleanProperty();

    public User() {

    }

    public User(String fullName){
        setUsername(fullName);
    }

    public User(String username, String fullName, String address, LocalDate dateOfBirth, String gender, String email, Boolean online, Boolean activated) {
        setUsername(username);
        setFullName(fullName);
        setAddress(address);
        setDateOfBirth(dateOfBirth);
        setGender(gender);
        setEmail(email);
        setOnline(online);
        setActivated(activated);
    }
}
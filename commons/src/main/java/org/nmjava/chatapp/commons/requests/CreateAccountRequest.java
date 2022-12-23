package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

import java.time.LocalDate;

@Getter
public class CreateAccountRequest extends Request {
    private String username;
    private String password;
    private String fullName;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;

    @Builder
    public CreateAccountRequest(@NonNull String username, @NonNull String password, @NonNull String fullName, @NonNull String address, @NonNull LocalDate dateOfBirth, @NonNull String gender, @NonNull String email) {
        super(RequestType.CREATE_ACCOUNT);
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
    }
}

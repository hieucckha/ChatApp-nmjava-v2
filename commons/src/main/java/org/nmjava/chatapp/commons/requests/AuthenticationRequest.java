package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class AuthenticationRequest extends Request {
    private String username;
    private String password;

    @Builder
    public AuthenticationRequest(@NonNull String username, @NonNull String password) {
        super(RequestType.AUTHENTICATION);
        this.username = username;
        this.password = password;
    }
}

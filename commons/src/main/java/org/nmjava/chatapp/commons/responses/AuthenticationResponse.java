package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class AuthenticationResponse extends Response {
    // 0 is admin
    // 1 is user
    private int role;

    @Builder
    public AuthenticationResponse(@NonNull StatusCode statusCode, int role) {
        super(ResponseType.AUTHENTICATION, statusCode);
        this.role = role;
    }
}
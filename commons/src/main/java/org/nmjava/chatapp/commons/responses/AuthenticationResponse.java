package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class AuthenticationResponse extends Response {

    @Builder
    public AuthenticationResponse(@NonNull StatusCode statusCode) {
        super(ResponseType.AUTHENTICATION, statusCode);
    }
}
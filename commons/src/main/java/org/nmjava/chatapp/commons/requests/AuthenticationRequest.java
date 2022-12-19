package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class AuthenticationRequest extends Request {
    @Builder
    public AuthenticationRequest() {
        super(RequestType.AUTHENTICATION);
    }

}

package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class ForgotPasswordRequest extends Request {
    private String email;

    @Builder
    public ForgotPasswordRequest(@NonNull String email) {
        super(RequestType.FORGOT_PASSWORD);
        this.email = email;
    }
}

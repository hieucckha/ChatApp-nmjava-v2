package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class CheckUserExistRequest extends Request {
    private String username;

    @Builder
    public CheckUserExistRequest(@NonNull String username) {
        super(RequestType.CHECK_USER_EXIST);
        this.username = username;
    }
}

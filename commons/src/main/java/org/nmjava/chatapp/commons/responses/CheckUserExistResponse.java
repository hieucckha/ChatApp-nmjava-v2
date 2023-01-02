package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class CheckUserExistResponse extends Response {
    private Boolean isExist;

    @Builder
    public CheckUserExistResponse(@NonNull StatusCode statusCode, @NonNull Boolean isExist) {
        super(ResponseType.CHECK_USER_EXIST, statusCode);
        this.isExist = isExist;
    }
}

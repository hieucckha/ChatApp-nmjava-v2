package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class CreateAccountResponse extends Response {
    @Builder
    CreateAccountResponse(@NonNull StatusCode statusCode) {
        super(ResponseType.CREATE_ACCOUNT, statusCode);
    }
}

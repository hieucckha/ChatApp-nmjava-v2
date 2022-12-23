package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class DeleteMessageResponse extends Response {
    @Builder
    public DeleteMessageResponse(@NonNull StatusCode statusCode) {
        super(ResponseType.DELETE_MESSAGE, statusCode);
    }
}

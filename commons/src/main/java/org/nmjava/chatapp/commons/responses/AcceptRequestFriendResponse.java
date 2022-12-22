package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class AcceptRequestFriendResponse extends Response {
    @Builder
    public AcceptRequestFriendResponse(@NonNull StatusCode statusCode) {
        super(ResponseType.ACCEPT_REQUEST_FRIEND, statusCode);
    }
}


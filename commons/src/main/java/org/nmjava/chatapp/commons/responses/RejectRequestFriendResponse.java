package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class RejectRequestFriendResponse extends Response {
    @Builder
    public RejectRequestFriendResponse(@NonNull StatusCode statusCode) {
        super(ResponseType.REJECT_REQUEST_FRIEND, statusCode);
    }
}

package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class AcceptRequestFriendResponse extends Response {
    private String user;
    private String friend;

    @Builder
    public AcceptRequestFriendResponse(@NonNull StatusCode statusCode, String user, String friend) {
        super(ResponseType.ACCEPT_REQUEST_FRIEND, statusCode);
        this.user = user;
        this.friend = friend;
    }
}


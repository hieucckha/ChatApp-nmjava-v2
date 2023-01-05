package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class UnfriendResponse extends Response {
    private String user;
    private String friend;

    @Builder
    public UnfriendResponse(@NonNull StatusCode statusCode, String user, String friend) {
        super(ResponseType.UNFRIEND, statusCode);
        this.user = user;
        this.friend = friend;
    }
}

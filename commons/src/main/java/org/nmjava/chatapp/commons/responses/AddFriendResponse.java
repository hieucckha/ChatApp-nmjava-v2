package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class AddFriendResponse extends Response {
    private String user;
    private String friend;

    @Builder
    public AddFriendResponse(@NonNull StatusCode statusCode, String user, String friend) {
        super(ResponseType.ADD_FRIEND, statusCode);
        this.user = user;
        this.friend = friend;
    }
}

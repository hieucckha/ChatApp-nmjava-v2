package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class UnfriendRequest extends Request {
    private String user;
    private String friend;

    @Builder
    public UnfriendRequest(@NonNull String username, @NonNull String friend) {
        super(RequestType.UNFRIEND);
        this.user = username;
        this.friend = friend;
    }
}

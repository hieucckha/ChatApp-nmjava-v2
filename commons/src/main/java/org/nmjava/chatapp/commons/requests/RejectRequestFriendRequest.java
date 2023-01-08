package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class RejectRequestFriendRequest extends Request {
    private String user;
    private String friend;

    @Builder
    public RejectRequestFriendRequest(@NonNull String user, @NonNull String friend) {
        super(RequestType.REJECT_REQUEST_FRIEND);
        this.user = user;
        this.friend = friend;
    }
}

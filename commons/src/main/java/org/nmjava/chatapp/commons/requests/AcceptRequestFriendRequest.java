package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class AcceptRequestFriendRequest extends Request {
    private String userID;
    private String usernameFriend;

    @Builder
    public AcceptRequestFriendRequest(@NonNull String userID, @NonNull String usernameFriend) {
        super(RequestType.ACCEPT_REQUEST_FRIEND);
        this.userID = userID;
        this.usernameFriend = usernameFriend;
    }
}

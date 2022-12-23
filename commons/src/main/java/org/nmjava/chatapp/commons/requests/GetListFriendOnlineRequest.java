package org.nmjava.chatapp.commons.requests;

import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GetListFriendOnlineRequest extends Request {
    private String username;

    public GetListFriendOnlineRequest(@NonNull String username) {
        super(RequestType.GET_LIST_FRIEND_ONLINE);
        this.username = username;
    }
}

package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GetListRequestFriendRequest extends Request {
    private String username;

    @Builder
    public GetListRequestFriendRequest(@NonNull String username) {
        super(RequestType.GET_LIST_REQUEST_FRIEND);
        this.username = username;
    }
}

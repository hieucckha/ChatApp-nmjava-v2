package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.models.Friend;

import java.util.Collection;

@Getter
public class GetListFriendResponse extends Response {
    private Collection<Friend> friends;

    @Builder
    GetListFriendResponse(@NonNull StatusCode statusCode, @NonNull Collection<Friend> friends) {
        super(ResponseType.GET_LIST_FRIEND, statusCode);
        this.friends = friends;
    }
}

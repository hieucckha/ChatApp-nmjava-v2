package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class RemoveUserGroupChatResponse extends Response {
    @Builder
    public RemoveUserGroupChatResponse(@NonNull StatusCode statusCode) {
        super(ResponseType.REMOVE_USER_GROUP_CHAT, statusCode);
    }
}

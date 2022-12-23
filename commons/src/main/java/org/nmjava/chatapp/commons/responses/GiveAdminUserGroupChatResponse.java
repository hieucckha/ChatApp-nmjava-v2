package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class GiveAdminUserGroupChatResponse extends Response {
    @Builder
    public GiveAdminUserGroupChatResponse(@NonNull StatusCode statusCode) {
        super(ResponseType.GIVE_ADMIN_USER_GROUP_CHAT, statusCode);
    }
}

package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class RemoveUserGroupChatResponse extends Response {
    private String conservationId;
    private String member;

    @Builder
    public RemoveUserGroupChatResponse(@NonNull StatusCode statusCode,@NonNull String conservationId,@NonNull String member) {
        super(ResponseType.REMOVE_USER_GROUP_CHAT, statusCode);
        this.conservationId = conservationId;
        this.member = member;
    }
}

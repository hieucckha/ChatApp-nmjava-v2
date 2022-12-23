package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;
@Getter
public class RenameGroupChatResponse extends Response {
    @Builder
    RenameGroupChatResponse(@NonNull StatusCode statusCode) {
        super(ResponseType.RENAME_GROUP_CHAT, statusCode);
    }
}

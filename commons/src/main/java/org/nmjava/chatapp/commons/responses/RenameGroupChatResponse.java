package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class RenameGroupChatResponse extends Response {
    private String conservationID;
    private String username;
    private String newName;

    @Builder
    RenameGroupChatResponse(@NonNull StatusCode statusCode,@NonNull String conservationID, @NonNull String username,@NonNull String newName) {
        super(ResponseType.RENAME_GROUP_CHAT, statusCode);
        this.conservationID = conservationID;
        this.username = username;
        this.newName = newName;
    }
}

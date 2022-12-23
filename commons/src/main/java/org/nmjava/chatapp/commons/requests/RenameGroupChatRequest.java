package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class RenameGroupChatRequest extends Request {
    private String conservationID;
    private String newName;

    @Builder
    public RenameGroupChatRequest(@NonNull String conservationID, @NonNull String newName) {
        super(RequestType.RENAME_GROUP_CHAT);
        this.conservationID = conservationID;
        this.newName = newName;
    }
}

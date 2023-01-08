package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class DeleteMessageRequest extends Request {
    private String conservationID;
    private String username;
    private String messageID;

    @Builder
    public DeleteMessageRequest(@NonNull String conservationID, @NonNull String username, @NonNull String messageID) {
        super(RequestType.DELETE_MESSAGE);
        this.conservationID = conservationID;
        this.username = username;
        this.messageID = messageID;
    }
}

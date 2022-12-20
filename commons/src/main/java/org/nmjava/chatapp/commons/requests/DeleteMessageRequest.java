package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class DeleteMessageRequest extends Request {
    private String conservationID;
    private String userID;
    private String messageID;

    @Builder
    public DeleteMessageRequest(@NonNull String conservationID, @NonNull String userID, @NonNull String messageID) {
        super(RequestType.DELETE_MESSAGE);
        this.conservationID = conservationID;
        this.userID = userID;
        this.messageID = messageID;
    }
}

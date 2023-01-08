package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class SendMessageRequest extends Request {
    private String conservationID;
    private String username;
    private String message;

    @Builder
    public SendMessageRequest(@NonNull String conservationID, @NonNull String username, @NonNull String message) {
        super(RequestType.SEND_MESSAGE);
        this.conservationID = conservationID;
        this.username = username;
        this.message = message;
    }
}

package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class SendMessageRequest extends Request {
    private String conservationID;
    private String senderID;
    private String message;

    @Builder
    public SendMessageRequest(@NonNull String conservationID, @NonNull String senderID, @NonNull String message) {
        super(RequestType.SEND_MESSAGE);
        this.conservationID = conservationID;
        this.senderID = senderID;
        this.message = message;
    }
}

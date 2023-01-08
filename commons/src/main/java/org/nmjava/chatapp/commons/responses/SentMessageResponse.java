package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class SentMessageResponse extends Response {
    private String conservationID;
    private String sender;
    private String message;

    @Builder
    public SentMessageResponse(@NonNull StatusCode statusCode, @NonNull String conservationID, @NonNull String sender, @NonNull String message) {
        super(ResponseType.SEND_MESSAGE, statusCode);
        this.conservationID = conservationID;
        this.sender = sender;
        this.message = message;
    }
}

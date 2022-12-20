package org.nmjava.chatapp.commons.requests;

import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class SearchMessageUserRequest extends Request {
    private String senderID;
    private String userID;
    private String text;

    public SearchMessageUserRequest(@NonNull String senderID, @NonNull String userID, @NonNull String text) {
        super(RequestType.SEARCH_MESSAGE_USER);
        this.senderID = senderID;
        this.userID = userID;
        this.text = text;
    }
}

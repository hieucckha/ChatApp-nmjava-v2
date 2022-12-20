package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class SearchMessageAllRequest extends Request {
    private String userID;
    private String text;

    @Builder
    public SearchMessageAllRequest(@NonNull String userID, @NonNull String text) {
        super(RequestType.SEARCH_MESSAGE_ALL);
        this.userID = userID;
        this.text = text;
    }
}

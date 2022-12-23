package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class SearchMessageAllRequest extends Request {
    private String username;
    private String text;

    @Builder
    public SearchMessageAllRequest(@NonNull String username, @NonNull String text) {
        super(RequestType.SEARCH_MESSAGE_ALL);
        this.username = username;
        this.text = text;
    }
}

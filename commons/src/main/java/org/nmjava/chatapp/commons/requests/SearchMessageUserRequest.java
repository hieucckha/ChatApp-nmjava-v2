package org.nmjava.chatapp.commons.requests;

import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class SearchMessageUserRequest extends Request {
    private String seeker;
    private String user;
    private String text;

    public SearchMessageUserRequest(@NonNull String seeker, @NonNull String user, @NonNull String text) {
        super(RequestType.SEARCH_MESSAGE_USER);
        this.seeker = seeker;
        this.user = user;
        this.text = text;
    }
}

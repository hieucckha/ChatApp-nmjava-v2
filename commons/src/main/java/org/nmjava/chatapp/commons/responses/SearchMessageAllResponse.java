package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.models.Message;

import java.util.Collection;

@Getter
public class SearchMessageAllResponse extends Response {
    private Collection<Message> messages;

    @Builder
    public SearchMessageAllResponse(@NonNull StatusCode statusCode, Collection<Message> messages) {
        super(ResponseType.SEARCH_MESSAGE_ALL, statusCode);
        this.messages = messages;
    }
}

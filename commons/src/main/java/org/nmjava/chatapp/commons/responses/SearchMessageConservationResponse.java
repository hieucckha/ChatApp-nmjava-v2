package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.models.Message;

import java.util.Collection;

@Getter
public class SearchMessageConservationResponse extends Response {
    private Collection<Message> messages;
    private String conservationID;

    @Builder
    public SearchMessageConservationResponse(@NonNull StatusCode statusCode, Collection<Message> messages , @NonNull String conservationID) {
        super(ResponseType.SEARCH_MESSAGE_CONSERVATION, statusCode);
        this.messages = messages;
        this.conservationID = conservationID;
    }
}

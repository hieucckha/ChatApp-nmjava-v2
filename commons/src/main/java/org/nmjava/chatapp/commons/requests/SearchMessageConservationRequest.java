package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class SearchMessageConservationRequest extends Request {
    private String conservationID;
    private String text;
    @Builder
    public SearchMessageConservationRequest(@NonNull String conservationID, @NonNull String text) {
        super(RequestType.SEARCH_MESSAGE_CONSERVATION);
        this.conservationID = conservationID;
        this.text = text;
    }
}

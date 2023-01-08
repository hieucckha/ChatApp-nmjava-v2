package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GetListMessageConservationRequest extends Request {
    private String username;
    private String conservationID;

    @Builder
    public GetListMessageConservationRequest(@NonNull String username, @NonNull String conservationID) {
        super(RequestType.GET_LIST_MESSAGE_CONSERVATION);
        this.username = username;
        this.conservationID = conservationID;
    }
}

package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GetListMessageConservationRequest extends Request {
    private String userID;
    private String conservationID;

    @Builder
    public GetListMessageConservationRequest(@NonNull RequestType type, @NonNull String userID, @NonNull String conservationID) {
        super(type);
        this.userID = userID;
        this.conservationID = conservationID;
    }
}

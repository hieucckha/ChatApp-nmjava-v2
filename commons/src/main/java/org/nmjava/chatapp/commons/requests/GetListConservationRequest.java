package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GetListConservationRequest extends Request {
    private String userID;

    @Builder
    public GetListConservationRequest(@NonNull String userID) {
        super(RequestType.GET_LIST_MESSAGE_CONSERVATION);
        this.userID = userID;
    }
}

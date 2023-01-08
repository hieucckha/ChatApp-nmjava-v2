package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GetListMemberConservationRequest extends Request {
    private String conservationID;

    @Builder
    public GetListMemberConservationRequest(@NonNull String conservationID) {
        super(RequestType.GET_LIST_MEMBER_CONSERVATION);
        this.conservationID = conservationID;
    }
}

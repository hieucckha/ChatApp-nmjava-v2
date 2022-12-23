package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GetListConservationRequest extends Request {
    private String username;

    @Builder
    public GetListConservationRequest(@NonNull String username) {
        super(RequestType.GET_LIST_CONSERVATION);
        this.username = username;
    }
}

package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

import java.util.Collection;

@Getter
public class GetListMemberConservationResponse extends Response {
    private Collection<String> users;

    @Builder
    public GetListMemberConservationResponse(@NonNull StatusCode statusCode, @NonNull Collection<String> users) {
        super(ResponseType.GET_LIST_MEMBER_CONSERVATION, statusCode);
        this.users = users;
    }
}

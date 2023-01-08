package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.models.Conservation;

import java.util.Collection;

@Getter
public class GetListConservationResponse extends Response {
    private Collection<Conservation> conservations;

    @Builder
    public GetListConservationResponse(@NonNull StatusCode statusCode, @NonNull Collection<Conservation> conservations) {
        super(ResponseType.GET_LIST_CONSERVATION, statusCode);
        this.conservations = conservations;
    }
}

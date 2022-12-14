package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.Action;

public class InformationRequest extends Request {
    @Builder
    public InformationRequest(@NonNull Action action) {
        super(action);
    }

}

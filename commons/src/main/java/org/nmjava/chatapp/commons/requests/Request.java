package org.nmjava.chatapp.commons.requests;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.nmjava.chatapp.commons.enums.RequestType;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Request implements Serializable {
    @NonNull
    protected RequestType type;
}

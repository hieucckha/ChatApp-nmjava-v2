package org.nmjava.chatapp.commons.responses;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Response implements Serializable {
    @NonNull
    protected ResponseType type;
    @NonNull
    protected StatusCode statusCode;
}

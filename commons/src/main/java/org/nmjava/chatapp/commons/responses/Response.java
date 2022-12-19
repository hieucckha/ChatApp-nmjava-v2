package org.nmjava.chatapp.commons.responses;

import lombok.Getter;
import lombok.Setter;
import org.nmjava.chatapp.commons.enums.Action;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

import java.io.Serializable;

@Getter
public abstract class Response implements Serializable {
    protected ResponseType type = ResponseType.DEFAULT;
    protected Action action;
    protected StatusCode statusCode;
}

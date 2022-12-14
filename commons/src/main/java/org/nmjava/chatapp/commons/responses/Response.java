package org.nmjava.chatapp.commons.responses;

import lombok.Getter;
import lombok.Setter;
import org.nmjava.chatapp.commons.enums.StatusCode;

import java.io.Serializable;

@Getter
public abstract class Response implements Serializable {
    protected StatusCode statusCode;
}

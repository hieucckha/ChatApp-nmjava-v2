package org.nmjava.chatapp.commons.requests;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.nmjava.chatapp.commons.enums.Action;

import java.io.Serializable;

@Setter
@Getter
@RequiredArgsConstructor
public abstract class Request implements Serializable {
    @NonNull
    protected Action action;
}

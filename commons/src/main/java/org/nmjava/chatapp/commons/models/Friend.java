package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Friend implements Serializable {
    private String username;
    private Boolean isFriend;

    public Friend(String username, Boolean isFriend) {
        setUsername(username);
        setIsFriend(isFriend);
    }
}

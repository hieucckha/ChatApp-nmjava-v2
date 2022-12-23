package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friend {
    private String username;
    private Boolean isFriend;

    public Friend(String username, Boolean isFriend) {
        setUsername(username);
        setIsFriend(isFriend);
    }
}

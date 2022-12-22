package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friend {
    private String friendID;
    private String username;
    private Boolean isFriend;

    public Friend(String friendID) {
        setFriendID(friendID);
    }

    public Friend(String friendID, String username) {
        setFriendID(friendID);
        setUsername(username);
    }
}

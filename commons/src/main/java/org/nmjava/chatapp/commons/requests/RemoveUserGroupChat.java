package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class RemoveUserGroupChat extends Request {
    private String conservationID;
    private String adminID;
    private String username;

    @Builder
    public RemoveUserGroupChat(@NonNull String conservationID, @NonNull String adminID, @NonNull String username) {
        super(RequestType.REMOVE_USER_GROUP_CHAT);
        this.conservationID = conservationID;
        this.adminID = adminID;
        this.username = username;
    }
}

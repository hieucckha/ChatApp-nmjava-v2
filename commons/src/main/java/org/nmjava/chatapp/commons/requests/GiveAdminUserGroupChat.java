package org.nmjava.chatapp.commons.requests;

import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GiveAdminUserGroupChat extends Request {
    private String conservationID;
    private String adminID;
    private String username;

    public GiveAdminUserGroupChat(@NonNull String conservationID, @NonNull String adminID, @NonNull String username) {
        super(RequestType.GIVE_ADMIN_USER_GROUP_CHAT);
        this.conservationID = conservationID;
        this.adminID = adminID;
        this.username = username;
    }
}

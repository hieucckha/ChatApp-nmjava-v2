package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class GiveAdminUserGroupChat extends Request {
    private String conservationID;
    private String admin;
    private String member;
    @Builder
    public GiveAdminUserGroupChat(@NonNull String conservationID, @NonNull String admin, @NonNull String member) {
        super(RequestType.GIVE_ADMIN_USER_GROUP_CHAT);
        this.conservationID = conservationID;
        this.admin = admin;
        this.member = member;
    }
}

package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class AddMemberGroupChatRequest extends Request {
    private String conservationID;
    private String adderID;
    private String username;

    @Builder
    public AddMemberGroupChatRequest(@NonNull String conservationID, @NonNull String adderID, @NonNull String username) {
        super(RequestType.ADD_MEMBER_GROUP_CHAT);
        this.conservationID = conservationID;
        this.adderID = adderID;
        this.username = username;
    }
}

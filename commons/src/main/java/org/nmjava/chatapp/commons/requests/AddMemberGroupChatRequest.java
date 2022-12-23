package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

@Getter
public class AddMemberGroupChatRequest extends Request {
    private String conservationID;
    private String adder;
    private String member;

    @Builder
    public AddMemberGroupChatRequest(@NonNull String conservationID, @NonNull String adder, @NonNull String member) {
        super(RequestType.ADD_MEMBER_GROUP_CHAT);
        this.conservationID = conservationID;
        this.adder = adder;
        this.member = member;
    }
}

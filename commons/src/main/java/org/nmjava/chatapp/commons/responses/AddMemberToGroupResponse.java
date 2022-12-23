package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;

@Getter
public class AddMemberToGroupResponse extends Response {
    private String conservationID;
    private String adder;
    private String member;

    @Builder
    public AddMemberToGroupResponse(@NonNull StatusCode statusCode, String conservationID, String adder, String member) {
        super(ResponseType.ADD_MEMBER_GROUP_CHAT, statusCode);
        this.conservationID = conservationID;
        this.adder = adder;
        this.member = member;
    }
}

package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

import java.util.List;

@Getter
public class CreateGroupChatRequest extends Request {
    private String creatorID;
    private List<String> usersID;

    @Builder
    public CreateGroupChatRequest(@NonNull String creatorID, @NonNull List<String> usersID) {
        super(RequestType.CREATE_GROUP_CHAT);
        this.creatorID = creatorID;
        this.usersID = usersID;
    }
}

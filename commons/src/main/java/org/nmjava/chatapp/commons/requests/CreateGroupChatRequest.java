package org.nmjava.chatapp.commons.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.RequestType;

import java.util.List;

@Getter
public class CreateGroupChatRequest extends Request {
    private String creator;
    private List<String> members;

    @Builder
    public CreateGroupChatRequest(@NonNull String creator, @NonNull List<String> members) {
        super(RequestType.CREATE_GROUP_CHAT);
        this.creator = creator;
        this.members = members;
    }
}

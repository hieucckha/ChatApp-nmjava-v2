package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.nmjava.chatapp.commons.enums.Action;
import org.nmjava.chatapp.commons.enums.StatusCode;

import java.util.List;

@Getter
public class UserOnlineResponse extends Response {
    protected String name = "UserOnlineResponse";

    private List<String> userIds;

    @Builder
    public UserOnlineResponse(List<String> userIds, @NonNull Action action, @NonNull StatusCode statusCode) {
        this.userIds = userIds;
        this.action = action;
        this.statusCode = statusCode;
    }
}
package org.nmjava.chatapp.commons.responses;

import lombok.Builder;
import lombok.Getter;
import org.nmjava.chatapp.commons.enums.StatusCode;

import java.util.List;

@Getter
public class UserOnlineResponse extends Response {
    private List<String> userIds;

    @Builder
    public UserOnlineResponse(List<String> userIds, StatusCode statusCode) {
        this.userIds = userIds;
        this.statusCode = statusCode;
    }
}
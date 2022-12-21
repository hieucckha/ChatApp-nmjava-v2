package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Conservation {
    private String conservationID;
    private String creatorID;
    private String name;
    private String lastMessage;
    private String lastSender;
    private LocalDateTime createAt;
    private Boolean isGroup;
}

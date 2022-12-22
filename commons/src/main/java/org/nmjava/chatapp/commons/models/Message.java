package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String messageID;
    private String conservationID;
    private String senderID;
    private String message;
}

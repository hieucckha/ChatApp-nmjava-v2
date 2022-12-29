package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class Message implements Serializable {
    private String messageID;
    private String sender;
    private String conservationID;
    private LocalDateTime createAt;
    private String message;

    public Message(String messageID, String sender, String conservationId, String message) {
        setMessageID(messageID);
        setSender(sender);
        setConservationID(conservationId);
        setMessage(message);
    }
}

package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

@Getter
@Setter
public class Message implements Serializable {
    private String messageID;
    private String sender;
    private String conservationID;
    private String createAt;
    private String message;

    public Message(String messageID, String sender, String conservationId,String create_at, String message) {
        setMessageID(messageID);
        setSender(sender);
        setConservationID(conservationId);
        setCreateAt(create_at);
        setMessage(message);
    }
}

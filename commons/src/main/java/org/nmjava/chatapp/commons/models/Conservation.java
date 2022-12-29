package org.nmjava.chatapp.commons.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class Conservation implements Serializable {
    private String conservationID;
    private String creatorID;
    private String name;
    private String lastMessage;
    private String lastSender;
    private LocalDateTime createAt;
    private Boolean isGroup;

    public Conservation(String conservationID, String name, String lastMessage) {
        setConservationID(conservationID);
        setName(name);
        setLastMessage(lastMessage);
    }
}

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
    private String full_name;

    private Integer role;

    public Conservation(String conservationID, String name, String lastMessage,Boolean isGroup) {
        setConservationID(conservationID);
        setName(name);
        setLastMessage(lastMessage);
        setIsGroup(isGroup);
    }

    public Conservation(String conservationID, String name, String full_name, Integer role) {
        this.conservationID = conservationID;
        this.name = name;
        this.full_name = full_name;
        this.role = role;
    }
}

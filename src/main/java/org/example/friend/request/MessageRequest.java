package org.example.friend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private long fromId;
    private long toId;
    private long teamId;
    private String text;
    private Integer chatType;
}

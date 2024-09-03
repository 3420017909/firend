package org.example.friend.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class TeamRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String password;
}

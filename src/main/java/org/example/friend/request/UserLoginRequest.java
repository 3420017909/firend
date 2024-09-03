package org.example.friend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 202406L;

    private String phone;
    private String username;
    private String password;

}

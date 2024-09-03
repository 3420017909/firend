package org.example.friend.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String phone;
    private String username;
    private String password;
    private String profile;
    private String gender;
    private String tage;
}

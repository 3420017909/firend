package org.example.friend.exception;

import lombok.Data;
import org.example.friend.response.Response;
import org.example.friend.response.ResponseEntity;
@Data
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 202406L;
    private final int code;
    private final String message;

    public CustomException(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

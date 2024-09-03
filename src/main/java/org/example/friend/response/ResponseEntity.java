package org.example.friend.response;

public class ResponseEntity {
    public static Response success(String msg, Object data){
        return new Response(200,msg,data);
    }
    public static Response error(Integer code, String msg){
        return new Response(code,msg,null);
    }
}

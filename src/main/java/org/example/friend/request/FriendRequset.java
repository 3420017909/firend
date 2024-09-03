package org.example.friend.request;

import java.io.Serializable;

public class FriendRequset implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private long fromid;
    private long receiveid;
    private String remake;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromid() {
        return fromid;
    }

    public void setFromid(long fromid) {
        this.fromid = fromid;
    }

    public long getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(long receiveid) {
        this.receiveid = receiveid;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }
}

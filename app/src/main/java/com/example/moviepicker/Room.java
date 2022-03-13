package com.example.moviepicker;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private String code;
    private String ownerId;
    private List<String> users;

    public Room(String push, String ownerId) {
        this.code = push.substring(1, 5).toUpperCase();
        this.ownerId = ownerId;
        this.users = new ArrayList<>();
        this.users.add(ownerId);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}

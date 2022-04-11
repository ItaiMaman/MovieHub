package com.example.moviepicker;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room {

    private String code;
    private String ownerId;
    private String roomId;
    private HashMap<String, String> users;
    private HashMap<String, Integer> swiped;
    private Boolean running, match;

    public Room(String push, String ownerId) {
        this.roomId = push;
        this.code = push.substring(push.length() - 4).toUpperCase();
        this.ownerId = ownerId;
        this.users = new HashMap<>();
        this.swiped = new HashMap<>();
        this.running = false;
        this.match = false;
    }

    public Room() {
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

    public HashMap<String, String> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, String> users) {
        this.users = users;
    }

    public HashMap<String, Integer> getSwiped() {
        return swiped;
    }

    public void setSwiped(HashMap<String, Integer> swiped) {
        this.swiped = swiped;
    }

    public Boolean getMatch() {
        return match;
    }

    public void setMatch(Boolean match) {
        this.match = match;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void addUser(String uid, String push){
        if(users == null)
            users = new HashMap<>();
        users.put(uid, push);
    }
}

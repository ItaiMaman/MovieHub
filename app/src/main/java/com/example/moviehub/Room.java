package com.example.moviehub;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Locale;

public class Room {

    private String code;
    private String ownerId;
    private String roomId;
    private int match;
    private HashMap<String, String> users;
    private HashMap<String, HashMap<String, Long>> swiped;
    private Boolean running;

    public Room(@NonNull String push, String ownerId) {
        this.roomId = push;
        this.code = "";
        for(int i = 0; i < push.length()-4; i++){
            char c = push.charAt(i+4);
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
                code += c;
            if(code.length() == 4){
                code = code.toUpperCase(Locale.ROOT);
                break;
            }

        }
        this.ownerId = ownerId;
        this.users = new HashMap<>();
        this.swiped = new HashMap<>();
        this.running = false;
        this.match = 0;
    }

    public Room() {}

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


    public HashMap<String, HashMap<String, Long>> getSwiped() {
        return swiped;
    }

    public void setSwiped(HashMap<String, HashMap<String, Long>> swiped) {
        this.swiped = swiped;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
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

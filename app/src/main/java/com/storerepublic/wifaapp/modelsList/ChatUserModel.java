package com.storerepublic.wifaapp.modelsList;

public class ChatUserModel {
    private boolean online;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public ChatUserModel(boolean online, String userId) {
        this.online = online;
        this.userId = userId;
    }

    public ChatUserModel() {
    }
}

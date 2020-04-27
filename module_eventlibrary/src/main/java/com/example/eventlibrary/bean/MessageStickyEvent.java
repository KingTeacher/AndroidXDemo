package com.example.eventlibrary.bean;

public class MessageStickyEvent {
    private String eventStr;

    public MessageStickyEvent(String eventStr) {
        this.eventStr = eventStr;
    }

    public String getEventStr() {
        return eventStr;
    }

    public void setEventStr(String eventStr) {
        this.eventStr = eventStr;
    }
}

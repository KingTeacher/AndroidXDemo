package com.example.eventlibrary.bean;

public  class MessageEvent {
    private String eventStr;

    public MessageEvent(String eventStr) {
        this.eventStr = eventStr;
    }

    public String getEventStr() {
        return eventStr;
    }

    public void setEventStr(String eventStr) {
        this.eventStr = eventStr;
    }
}

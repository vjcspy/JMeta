package com.vjcspy.eventmanager;

import lombok.Data;

import java.util.UUID;

@Data
public class EventAction<P> {
    private UUID eventId;
    private String type;
    private P payload;

    public EventAction(String type, P payload) {
        this.type = type;
        this.payload = payload;
        this.eventId = UUID.randomUUID();
    }
}

package com.vjcspy.eventmanager;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EventAction<P> {
    private UUID eventId;
    private final String type;
    private final P payload;

    public EventAction(String type, P payload) {
        this.type = type;
        this.payload = payload;
    }

    public void registerEventId(UUID eventId) {
        if (this.eventId == null) {
            this.eventId = eventId != null ? eventId : UUID.randomUUID();
        }
    }
}

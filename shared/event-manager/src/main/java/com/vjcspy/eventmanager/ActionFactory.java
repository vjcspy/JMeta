package com.vjcspy.eventmanager;

@FunctionalInterface
public interface ActionFactory<P> {
    EventAction<P> create(P payload);
}


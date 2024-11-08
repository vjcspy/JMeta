package com.vjcspy.spring.tedbed.eventmanager;

import com.vjcspy.eventmanager.ActionFactory;
import com.vjcspy.eventmanager.EventActionFactory;
import jakarta.validation.constraints.Null;

public class FooAction {
    public static final ActionFactory<Null> FOO = EventActionFactory.create("FOO");
}

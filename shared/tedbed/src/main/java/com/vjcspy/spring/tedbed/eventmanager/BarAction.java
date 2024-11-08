package com.vjcspy.spring.tedbed.eventmanager;

import com.vjcspy.eventmanager.ActionFactory;
import com.vjcspy.eventmanager.EventActionFactory;
import jakarta.validation.constraints.Null;

public class BarAction {
    public static final ActionFactory<Null> BAR = EventActionFactory.create("BAR");
}

package com.vjcspy.spring.tedbed.eventmanager;

import com.vjcspy.eventmanager.ActionFactory;
import com.vjcspy.eventmanager.EventActionFactory;
import jakarta.validation.constraints.Null;

public final class Actions {
    public static final String TYPE_BAR = "BAR";
    public static final String TYPE_FOO = "FOO";

    public static final class Bar {
        public static final ActionFactory<Null> BAR = EventActionFactory.create(TYPE_BAR);

        private Bar() {
        }
    }

    public static final class Foo {
        public static final ActionFactory<Null> FOO = EventActionFactory.create(TYPE_FOO);

        private Foo() {
        }
    }

    private Actions() {
    }
}
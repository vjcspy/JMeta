package com.vjcspy.spring.base.annotation.eventmanager;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {
    /**
     * Event types that this handler will process
     */
    String[] type();
}
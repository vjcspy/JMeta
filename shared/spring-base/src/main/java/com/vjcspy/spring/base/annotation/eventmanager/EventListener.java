package com.vjcspy.spring.base.annotation.eventmanager;

import java.lang.annotation.*;

/**
 * Annotation to mark methods that should be registered as EventHandlers.
 * The method must return an EventHandler.
 * <p>
 * Example usage:
 * <pre>{@code
 * @Component
 * public class UserEventHandlers {
 *     @Effect(type = {"CREATE_USER", "UPDATE_USER"})
 *     public EventHandler handleUserEvents() {
 *         return upstream -> upstream
 *             .map(event -> {
 *                 // process event
 *                 return new EventAction<>("USER_PROCESSED", result);
 *             });
 *     }
 * }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {
    /**
     * Event types that this handler will process
     */
    String[] type();
}
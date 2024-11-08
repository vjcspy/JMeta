package com.vjcspy.eventmanager;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A reactive event management system that handles event dispatching and processing using RxJava.
 * This class implements the Singleton pattern and uses PublishSubject for event distribution.
 * <p>
 * Example usage:
 * <pre>{@code
 * // Get instance of EventManager
 * EventManager manager = EventManager.getInstance();
 *
 * // Create an event handler for user creation
 * EventHandler userCreationHandler = upstream -> upstream
 *     .map(event -> {
 *         UserPayload payload = (UserPayload) event.getPayload();
 *         // Process user creation logic
 *         return new EventAction<>("USER_CREATED", processedPayload);
 *     })
 *     .doOnError(error -> log.error("Error processing user creation", error));
 *
 * // Register the handler for CREATE_USER events
 * manager.registerEvent("CREATE_USER", userCreationHandler);
 *
 * // Dispatch a CREATE_USER event
 * UserPayload payload = new UserPayload("John Doe");
 * manager.dispatch(new EventAction<>("CREATE_USER", payload));
 *
 * // Example with multiple event types and complex processing
 * EventHandler complexHandler = upstream -> upstream
 *     .buffer(5, TimeUnit.SECONDS, 10)
 *     .map(events -> processBatch(events))
 *     .map(result -> new EventAction<>("BATCH_RESULT", result));
 *
 * manager.registerEvent(
 *     List.of("EVENT_TYPE_1", "EVENT_TYPE_2"),
 *     complexHandler
 * );
 * }</pre>
 */
@Slf4j
public class EventManager {
    private final PublishSubject<EventAction<?>> subject = PublishSubject.create();
    private static final EventManager INSTANCE = new EventManager();

    private EventManager() {
    }

    /**
     * Gets the singleton instance of EventManager.
     *
     * @return the singleton instance of EventManager
     */
    public static EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * Dispatches an event to all registered handlers.
     * The event will be processed asynchronously by handlers that are registered for its type.
     *
     * @param event the event to dispatch
     * @param <P>   the type of the event payload
     *              <p>
     *              Example:
     *              <pre>{@code
     *                           UserPayload payload = new UserPayload("John");
     *                           EventAction<UserPayload> event = new EventAction<>("CREATE_USER", payload);
     *                           manager.dispatch(event);
     *                           }</pre>
     */
    public <P> void dispatch(EventAction<P> event) {
        log.debug("Dispatching event: {} with payload: {}", event.getType(), event.getPayload());
        subject.onNext(event);
    }

    /**
     * Registers a handler for a single event type.
     * This is a convenience method that delegates to {@link #registerEvent(List, EventHandler)}.
     *
     * @param eventType    the type of event to handle
     * @param eventHandler the handler to process events
     *                     <p>
     *                     Example:
     *                     <pre>{@code
     *                                         EventHandler handler = upstream -> upstream
     *                                             .map(event -> new EventAction<>("PROCESSED", event.getPayload()));
     *                                         manager.registerEvent("MY_EVENT", handler);
     *                                         }</pre>
     */
    public void registerEvent(String eventType, EventHandler eventHandler) {
        registerEvent(List.of(eventType), eventHandler);
    }

    /**
     * Registers a handler for multiple event types.
     * The handler will be invoked for any event matching the specified types.
     * Events are processed asynchronously using computation scheduler.
     *
     * @param eventTypes   list of event types to handle
     * @param eventHandler the handler to process events
     *                     <p>
     *                     Example:
     *                     <pre>{@code
     *                                         EventHandler handler = upstream -> upstream
     *                                             .buffer(5, TimeUnit.SECONDS)
     *                                             .map(events -> processBatch(events))
     *                                             .map(result -> new EventAction<>("BATCH_RESULT", result));
     *
     *                                         manager.registerEvent(
     *                                             List.of("TYPE_1", "TYPE_2"),
     *                                             handler
     *                                         );
     *                                         }</pre>
     */
    public void registerEvent(List<String> eventTypes, EventHandler eventHandler) {
        log.info("Registering event: {}", eventTypes);

        subject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .filter(eventAction -> eventTypes.contains(eventAction.getType()))
                .compose(eventHandler)
                .subscribe(
                        new DisposableObserver<>() {
                            @Override
                            public void onNext(@NonNull EventAction<?> eventAction) {
                                dispatch(eventAction);
                            }

                            @Override
                            public void onError(@NotNull Throwable error) {
                                log.error("Error in event stream: {}", error.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                log.error("Why Completed???");
                            }
                        }
                );
    }
}
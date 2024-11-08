package com.vjcspy.eventmanager;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class EventManager {
    private final PublishSubject<EventAction<?>> subject = PublishSubject.create();
    private static final EventManager INSTANCE = new EventManager();

    private EventManager() {
    }

    public static EventManager getInstance() {
        return INSTANCE;
    }

    public <P> void dispatch(EventAction<P> event) {
        dispatch(event, null);
    }

    public <P> void dispatch(EventAction<P> event, UUID referenceEventId) {
        event.registerEventId(referenceEventId);
        subject.onNext(event);
        log.info("Dispatching event: {} with id: {}", event.getType(), event.getEventId());
    }

    public void registerEvent(List<String> eventTypes, EventHandler eventHandler) {
        log.info("Registering event: {}", eventTypes);

        subject.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .filter(eventAction -> eventTypes.contains(eventAction.getType()))
                .flatMap(originalEvent ->
                        Observable.just(originalEvent)
                                .compose(eventHandler)
                                .map(handledEvent -> Arrays.asList(originalEvent, handledEvent))
                )
                .subscribe(new DisposableObserver<>() {
                    @Override
                    public void onNext(@NonNull List<EventAction<?>> events) {
                        EventAction<?> originEvent = events.get(0);
                        EventAction<?> handledEvent = events.get(1);

                        if (originEvent.getEventId() != null && handledEvent.getEventId() != null && !originEvent.getEventId().equals(handledEvent.getEventId())) {
                            log.warn("Origin Event và Handled Event không cùng Id");
                        }

                        dispatch(handledEvent, originEvent.getEventId());
                    }

                    @Override
                    public void onError(@NotNull Throwable error) {
                        log.error("Error in event stream: {}", error.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        log.error("Why Completed???");
                    }
                });
    }
}
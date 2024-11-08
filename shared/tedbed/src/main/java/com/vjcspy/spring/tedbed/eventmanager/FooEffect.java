package com.vjcspy.spring.tedbed.eventmanager;

import com.vjcspy.eventmanager.EventHandler;
import com.vjcspy.spring.base.annotation.eventmanager.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FooEffect {
    @EventListener(type = {Actions.TYPE_FOO})
    public EventHandler handleUserEvents() {
        return upstream -> upstream
                .map(event -> {
                    log.info("Processing user event: {}", event.getType());
                    return Actions.Bar.BAR.create(null);
                });
    }
}

package com.vjcspy.spring.tedbed.eventmanager;

import com.vjcspy.eventmanager.EventHandler;
import com.vjcspy.spring.base.annotation.eventmanager.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FooEffect {
    @EventListener(type = {"FOO"})
    public EventHandler handleFoo() {
        return upstream -> upstream
                .map(event -> {
                    log.info("Processing user event: {}", event.getType());
                    return BarAction.BAR.create(null);
                });
    }
}

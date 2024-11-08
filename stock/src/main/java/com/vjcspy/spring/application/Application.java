package com.vjcspy.spring.application;

import com.vjcspy.eventmanager.EventManager;
import com.vjcspy.spring.tedbed.eventmanager.FooAction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication(scanBasePackages = "com.vjcspy.spring")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        testRxEvent();
    }

    private void testRxEvent() {
        EventManager.getInstance().dispatch(FooAction.FOO.create(null));
    }
}
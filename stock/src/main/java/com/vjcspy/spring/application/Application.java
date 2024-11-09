/* (C) 2024 */
package com.vjcspy.spring.application;

import com.vjcspy.eventmanager.EventManager;
import com.vjcspy.spring.tedbed.eventmanager.Actions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static net.logstash.logback.argument.StructuredArguments.value;

//import static net.logstash.logback.argument.StructuredArguments.*;

@SpringBootApplication(scanBasePackages = "com.vjcspy.spring")
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        log.info("Processing payment",
                kv("payment", Map.of(
                        "id", 123,
                        "amount", 45,
                        "method", 234,
                        "status", 123,
                        "metadata", 44
                ))
        );
        log.info("Processing order",
                kv("orderId", "aa"),
                kv("customerId", "bb"),
                kv("amount", "ccc")
        );
        log.info("Order {} processed for customer {}",
                value("orderId", "123"),
                value("customerId", "456")
        );
        testRxEvent();
    }

    private void testRxEvent() {
        EventManager.getInstance().dispatch(Actions.Foo.FOO.create(null));
    }
}

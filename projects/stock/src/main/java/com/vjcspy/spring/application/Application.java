/* (C) 2024 */
package com.vjcspy.spring.application;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static net.logstash.logback.argument.StructuredArguments.value;

import com.vjcspy.eventmanager.EventManager;
import com.vjcspy.spring.tedbed.eventmanager.Actions;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.vjcspy.spring")
@Slf4j
@EntityScan(basePackages = "com.vjcspy.spring")
@EnableJpaRepositories(basePackages = "com.vjcspy.spring")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        log.info(
                "Processing payment",
                kv(
                        "payment",
                        Map.of(
                                "id", 123,
                                "amount", 45,
                                "method", 234,
                                "status", 123,
                                "metadata", 44)));
        log.info("Processing order", kv("orderId", "aa"), kv("customerId", "bb"), kv("amount", "ccc"));
        log.info("Order {} processed for customer {}", value("orderId", "123"), value("customerId", "456"));
        testRxEvent();
    }

    private void testRxEvent() {
        EventManager.getInstance().dispatch(Actions.Foo.FOO.create(null));
    }
}

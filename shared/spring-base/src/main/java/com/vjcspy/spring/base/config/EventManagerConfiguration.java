package com.vjcspy.spring.base.config;

import com.vjcspy.eventmanager.EventHandler;
import com.vjcspy.eventmanager.EventManager;
import com.vjcspy.spring.base.annotation.eventmanager.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Configuration
public class EventManagerConfiguration {

    @Bean
    public EventManager eventManager() {
        return EventManager.getInstance();
    }

    @Bean
    public EventHandlerInitializer eventHandlerInitializer() {
        return new EventHandlerInitializer();
    }
}

@Slf4j
class EventHandlerInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final EventManager eventManager = EventManager.getInstance();
    private boolean initialized = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Tránh khởi tạo nhiều lần trong trường hợp có nhiều context refresh
        if (!initialized) {
            log.info("Initializing Event Handlers");
            ApplicationContext applicationContext = event.getApplicationContext();
            registerEventHandlers(applicationContext);
            initialized = true;
        }
    }

    private void registerEventHandlers(ApplicationContext applicationContext) {
        log.info("Scanning for @EventListener methods");

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            try {
                Object bean = applicationContext.getBean(beanName);
                ReflectionUtils.doWithMethods(
                        bean.getClass(),
                        method -> registerEventHandler(bean, method),
                        method -> method.isAnnotationPresent(EventListener.class)
                );
            } catch (Exception e) {
                log.debug("Could not process bean: {}", beanName, e);
            }
        }
    }

    private void registerEventHandler(Object bean, Method method) {
        try {
            EventListener annotation = method.getAnnotation(EventListener.class);
            if (annotation != null) {
                method.setAccessible(true);
                EventHandler handler = (EventHandler) method.invoke(bean);
                List<String> eventTypes = Arrays.asList(annotation.type());

                log.info("Registering event handler for types: {} from bean: {}",
                        eventTypes, bean.getClass().getSimpleName());
                eventManager.registerEvent(eventTypes, handler);
            }
        } catch (Exception e) {
            log.error("Failed to register event handler for method: {} in bean: {}",
                    method.getName(), bean.getClass().getSimpleName(), e);
        }
    }
}
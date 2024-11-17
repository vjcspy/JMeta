/* (C) 2024 */
package com.vjcspy.spring.application

import com.vjcspy.rxevent.RxEventManager
import com.vjcspy.spring.tedbed.rxeventmanager.TestBedAction
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

private val logger = KotlinLogging.logger {}

@SpringBootApplication(scanBasePackages = ["com.vjcspy.spring"])
@EntityScan(basePackages = ["com.vjcspy.spring"])
@EnableJpaRepositories(basePackages = ["com.vjcspy.spring"])
class Application {
    @EventListener(ApplicationReadyEvent::class)
    fun runAfterStartup() {
        testRxEvent()
    }

    private fun testRxEvent() {
        RxEventManager.dispatch(TestBedAction.FOO_ACTION.invoke(null))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.application

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.rxevent.RxEventManager
import com.vjcspy.spring.base.config.Env
import com.vjcspy.spring.tedbed.rxeventmanager.TestBedAction
import net.logstash.logback.argument.StructuredArguments.value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.vjcspy.spring"])
@EntityScan(basePackages = ["com.vjcspy.spring"])
@EnableJpaRepositories(basePackages = ["com.vjcspy.spring"])
class Application(
    private val env: Env,
) {
    private val logger = KtLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    fun runAfterStartup() {
        logger.info("Application `{}` started ", value("appName", env.get("APP_NAME")))

        testRxEvent()
    }

    private fun testRxEvent() {
        RxEventManager.dispatch(TestBedAction.FOO_ACTION(null))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

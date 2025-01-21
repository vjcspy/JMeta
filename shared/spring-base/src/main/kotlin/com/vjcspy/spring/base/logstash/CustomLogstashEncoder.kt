package com.vjcspy.spring.base.logstash

import ch.qos.logback.classic.spi.ILoggingEvent
import net.logstash.logback.LogstashFormatter
import net.logstash.logback.composite.AbstractCompositeJsonFormatter
import net.logstash.logback.encoder.LogstashEncoder

class CustomLogstashEncoder : LogstashEncoder() {
    override fun createFormatter(): AbstractCompositeJsonFormatter<ILoggingEvent> {
        val formatter = super.createFormatter()

        if (formatter is LogstashFormatter) {
            formatter.setIncludeContext(false)
            formatter.addProvider(CustomContextJsonProvider())
        }

        return formatter
    }
}

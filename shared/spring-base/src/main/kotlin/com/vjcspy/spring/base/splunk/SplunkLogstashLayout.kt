package com.vjcspy.spring.base.splunk

import ch.qos.logback.classic.spi.ILoggingEvent
import com.fasterxml.jackson.core.JsonGenerator
import net.logstash.logback.composite.AbstractJsonProvider
import net.logstash.logback.layout.LogstashLayout

class SplunkLogstashLayout : LogstashLayout() {
    override fun start() {
        addProvider(CustomJsonProvider())
        super.start()
    }

    private class CustomJsonProvider : AbstractJsonProvider<ILoggingEvent>() {
        override fun writeTo(
            generator: JsonGenerator,
            event: ILoggingEvent,
        ) {
            try {
                // Thêm custom timestamp
//                generator.writeStringField(
//                    "custom_timestamp",
//                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
//                        .format(Date(event.timeStamp)),
//                )

//                event.mdcPropertyMap.forEach { (key, value) ->
//                    generator.writeStringField(key, value)
//                }
//
//                // Thêm thông tin caller nếu có
//                event.callerData?.firstOrNull()?.let { caller ->
//                    generator.writeObjectFieldStart("caller")
//                    generator.writeStringField("class", caller.className)
//                    generator.writeStringField("method", caller.methodName)
//                    generator.writeStringField("file", caller.fileName)
//                    generator.writeNumberField("line", caller.lineNumber.toLong())
//                    generator.writeEndObject()
//                }
//
//                // Thêm exception info nếu có
//                event.throwableProxy?.let { throwable ->
//                    generator.writeObjectFieldStart("exception")
//                    generator.writeStringField("class", throwable.className)
//                    generator.writeStringField("message", throwable.message)
//                    generator.writeEndObject()
//                }
            } catch (e: Exception) {
                // Log error nhưng không throw exception để tránh ảnh hưởng đến logging
                println("Error in CustomJsonProvider: ${e.message}")
            }
        }
    }
}

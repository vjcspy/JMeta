package com.vjcspy.spring.base.logstash

import ch.qos.logback.core.spi.DeferredProcessingAware
import com.fasterxml.jackson.core.JsonGenerator
import net.logstash.logback.composite.ContextJsonProvider
import net.logstash.logback.composite.JsonWritingUtils

class CustomContextJsonProvider<Event : DeferredProcessingAware> : ContextJsonProvider<Event>() {
    override fun writeTo(generator: JsonGenerator, event: Event) {
        if (this.context != null) {
            if (this.fieldName != null) {
                generator.writeObjectFieldStart(this.fieldName)
            }

            val modifiedMap = HashMap(this.context.copyOfPropertyMap)
            // Xóa key ở đây
            modifiedMap.remove("splunkHecToken")
            modifiedMap.remove("splunkHecUrl")

            JsonWritingUtils.writeMapEntries(generator, modifiedMap)

            if (this.fieldName != null) {
                generator.writeEndObject()
            }
        }
    }
}

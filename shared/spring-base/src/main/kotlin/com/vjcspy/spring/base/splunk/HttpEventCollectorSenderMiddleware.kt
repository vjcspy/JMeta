package com.vjcspy.spring.base.splunk

import com.splunk.logging.HttpEventCollectorEventInfo
import com.splunk.logging.HttpEventCollectorMiddleware
import com.splunk.logging.HttpEventCollectorMiddleware.IHttpSender
import com.splunk.logging.HttpEventCollectorMiddleware.IHttpSenderCallback

class HttpEventCollectorSenderMiddleware : HttpEventCollectorMiddleware.HttpSenderMiddleware() {
    override fun postEvents(
        p0: MutableList<HttpEventCollectorEventInfo>?,
        p1: IHttpSender?,
        p2: IHttpSenderCallback?,
    ) {
        callNext(p0, p1, p0?.let { p1?.let { it1 -> p2?.let { it2 -> Callback(it, it1, it2) } } })
    }

    private inner class Callback(
        private val events: List<HttpEventCollectorEventInfo>,
        private val sender: IHttpSender,
        private val prevCallback: IHttpSenderCallback,
    ) : IHttpSenderCallback {
        override fun completed(
            statusCode: Int,
            reply: String,
        ) {
            prevCallback.completed(statusCode, reply)
        }

        override fun failed(ex: Exception) {
            prevCallback.failed(ex)
        }
    }
}

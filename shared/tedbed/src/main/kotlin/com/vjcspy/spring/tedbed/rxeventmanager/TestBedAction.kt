package com.vjcspy.spring.tedbed.rxeventmanager

import com.vjcspy.rxevent.eventActionFactory

class TestBedAction private constructor() {
    companion object {
        const val BAR_TYPE = "testbed.bar"
        const val FOO_TYPE = "testbed.foo"

        val BAR_ACTION = eventActionFactory<Any?>(BAR_TYPE)
        val FOO_ACTION = eventActionFactory<Any?>(FOO_TYPE)
    }
}

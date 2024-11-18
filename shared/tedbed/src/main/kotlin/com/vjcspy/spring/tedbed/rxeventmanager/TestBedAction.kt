package com.vjcspy.spring.tedbed.rxeventmanager

import com.vjcspy.rxevent.rxEventActionFactory

class TestBedAction private constructor() {
    companion object {
        const val BAR_TYPE = "testbed.bar"
        const val FOO_TYPE = "testbed.foo"

        val BAR_ACTION = rxEventActionFactory<Any?>(BAR_TYPE)
        val FOO_ACTION = rxEventActionFactory<Any?>(FOO_TYPE)
    }
}

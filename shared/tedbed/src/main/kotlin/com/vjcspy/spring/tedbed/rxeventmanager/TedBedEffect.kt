package com.vjcspy.spring.tedbed.rxeventmanager

import com.vjcspy.rxevent.RxEventHandler
import com.vjcspy.spring.base.annotation.rxevent.Effect

class TedBedEffect {
    @Effect(types = [TestBedAction.FOO_TYPE])
    fun handleUserEvents(): RxEventHandler =
        RxEventHandler { upstream ->
            upstream.map {
                TestBedAction.BAR_ACTION(null)
            }
        }
}

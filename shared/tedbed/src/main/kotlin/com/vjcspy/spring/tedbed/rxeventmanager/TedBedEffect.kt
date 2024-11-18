package com.vjcspy.spring.tedbed.rxeventmanager

import com.vjcspy.rxevent.RxEventHandler
import com.vjcspy.spring.base.annotation.rxevent.Effect
import org.springframework.stereotype.Component


@Component
class TedBedEffect {

    @Effect(types = [TestBedAction.FOO_TYPE])
    fun handleUserEvents(): RxEventHandler {
        return RxEventHandler { upstream ->
            upstream.map {
                TestBedAction.BAR_ACTION(null)
            }
        }
    }
}

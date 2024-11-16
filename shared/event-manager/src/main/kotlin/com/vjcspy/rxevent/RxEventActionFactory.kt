package com.vjcspy.rxevent

fun rxEventActionFactory(type: String): (Any) -> RxEventAction {
    return { payload ->
        RxEventAction(type, payload)
    }
}
package com.vjcspy.rxevent

fun rxEventActionFactory(type: String): (Any?) -> RxEventAction {
    return { payload ->
        RxEventAction(type, payload)
    }
}

// Extension property để lấy type
val ((Any?) -> RxEventAction).type: String
    get() = this(null).type

package com.vjcspy.rxevent

typealias EventActionFactory = (Map<String, Any>?) -> RxEventAction

fun rxEventActionFactory(type: String): EventActionFactory =
    { payload ->
        RxEventAction(type, payload)
    }

// Extension property để lấy type
val EventActionFactory.type: String
    get() = this(null).type

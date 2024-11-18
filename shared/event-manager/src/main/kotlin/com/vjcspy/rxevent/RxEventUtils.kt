package com.vjcspy.rxevent

import io.reactivex.rxjava3.core.Observable

fun Observable<RxEventAction<Any?>>.ofAction(type: Array<EventActionFactory<Any?>>): Observable<RxEventAction<Any?>> =
    this.filter { action ->
        type.map { it.type }.contains(action.type)
    }

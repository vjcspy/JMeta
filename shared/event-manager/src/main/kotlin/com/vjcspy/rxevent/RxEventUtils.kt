package com.vjcspy.rxevent

import io.reactivex.rxjava3.core.Observable

fun Observable<EventAction<Any?>>.ofAction(
    vararg actions: EventActionFactory<Any?>,
): Observable<EventAction<Any?>> =
    this.filter { action ->
        actions.map { it.type }.contains(action.type)
    }

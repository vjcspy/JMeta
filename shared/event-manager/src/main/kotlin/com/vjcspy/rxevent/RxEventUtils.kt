package com.vjcspy.rxevent

import io.reactivex.rxjava3.core.Observable

fun Observable<RxEventAction<Any?>>.ofAction(
    vararg actions: EventActionFactory<Any?>,
): Observable<RxEventAction<Any?>> =
    this.filter { action ->
        actions.map { it.type }.contains(action.type)
    }

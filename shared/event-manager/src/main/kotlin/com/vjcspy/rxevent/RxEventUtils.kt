package com.vjcspy.rxevent

import io.reactivex.rxjava3.core.Observable

// Hàm transform
fun Observable<RxEventAction>.ofAction(type: Array<EventActionFactory>): Observable<RxEventAction> =
    this.filter { action ->
        type.map { it.type }.contains(action.type)
    }

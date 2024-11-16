package com.vjcspy.rxevent

import io.reactivex.rxjava3.core.ObservableTransformer

typealias RxEventHandler = ObservableTransformer<RxEventAction, RxEventAction>
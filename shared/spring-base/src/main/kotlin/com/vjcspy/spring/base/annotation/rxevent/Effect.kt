package com.vjcspy.spring.base.annotation.rxevent

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Effect(val types: Array<String>)

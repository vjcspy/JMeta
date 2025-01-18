import org.reactivestreams.Subscription
import org.slf4j.MDC
import reactor.core.CoreSubscriber
import reactor.util.context.Context

class MDCContextLifter<T>(
    private val subscriber: CoreSubscriber<T>,
) : CoreSubscriber<T> {
    private val mdcContext: Map<String, String>? = MDC.getCopyOfContextMap()

    override fun currentContext(): Context = subscriber.currentContext()

    override fun onSubscribe(subscription: Subscription) {
        subscriber.onSubscribe(subscription)
    }

    override fun onNext(t: T) {
        updateMdc(mdcContext)
        try {
            subscriber.onNext(t)
        } finally {
            restoreMdc(mdcContext)
        }
    }

    override fun onError(throwable: Throwable) {
        updateMdc(mdcContext)
        try {
            subscriber.onError(throwable)
        } finally {
            restoreMdc(mdcContext)
        }
    }

    override fun onComplete() {
        updateMdc(mdcContext)
        try {
            subscriber.onComplete()
        } finally {
            restoreMdc(mdcContext)
        }
    }

    private fun updateMdc(context: Map<String, String>?) {
        if (context != null) {
            MDC.setContextMap(context)
        } else {
            MDC.clear()
        }

        // Copy context from Reactor Context to MDC
        currentContext().forEach { key, value ->
            if (key is String && value is String) {
                MDC.put(key.toString(), value.toString())
            }
        }
    }

    private fun restoreMdc(context: Map<String, String>?) {
        if (context != null) {
            MDC.setContextMap(context)
        } else {
            MDC.clear()
        }
    }
}

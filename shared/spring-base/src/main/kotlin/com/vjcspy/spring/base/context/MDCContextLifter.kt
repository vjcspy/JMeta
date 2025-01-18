import org.reactivestreams.Subscription
import org.slf4j.MDC
import reactor.core.CoreSubscriber
import reactor.util.context.Context

class MDCContextLifter<T>(
    private val subscriber: CoreSubscriber<T>,
) : CoreSubscriber<T> {
    override fun currentContext(): Context = subscriber.currentContext()

    override fun onSubscribe(subscription: Subscription) {
        subscriber.onSubscribe(subscription)
    }

    override fun onNext(t: T) {
        updateMdc()
        subscriber.onNext(t)
    }

    override fun onError(throwable: Throwable) {
        updateMdc()
        subscriber.onError(throwable)
    }

    override fun onComplete() {
        updateMdc()
        subscriber.onComplete()
    }

    private fun updateMdc() {
        // Copy context from Reactor Context to MDC
        currentContext().forEach { key, value ->
            if (key is String && value is String) {
                MDC.put(key.toString(), value.toString())
            }
        }
    }
}

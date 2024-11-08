package com.vjcspy.eventmanager;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;

/**
 * Ví dụ cách sử dụng EventHandler trong EventManager:
 *
 * <pre>
 * {@code
 * public class Example {
 *     public void example() {
 *         EventManager manager = EventManager.getInstance();
 *
 *         // Định nghĩa handler cho user creation
 *         EventHandler userHandler = upstream -> upstream
 *             .map(event -> {
 *                 UserPayload payload = (UserPayload) event.getPayload();
 *                 // Xử lý logic
 *                 return new EventAction<>("USER_CREATED", payload);
 *             })
 *             .doOnNext(event -> log.info("Processed user event: {}", event))
 *             .doOnError(error -> log.error("Error processing user event: ", error));
 *
 *         // Đăng ký handler
 *         manager.registerEvent("CREATE_USER", userHandler);
 *
 *         // Handler phức tạp hơn với retry và timeout
 *         EventHandler complexHandler = upstream -> upstream
 *             .flatMap(event ->
 *                 Observable.just(event)
 *                     .map(e -> {
 *                         // Xử lý logic phức tạp
 *                         return new EventAction<>("COMPLEX_RESULT", processComplexLogic(e));
 *                     })
 *                     .retry(3)
 *                     .timeout(5, TimeUnit.SECONDS)
 *             )
 *             .doOnError(error -> log.error("Complex handler error: ", error));
 *
 *         // Đăng ký nhiều event types cho một handler
 *         manager.registerEvent(
 *             List.of("COMPLEX_EVENT_1", "COMPLEX_EVENT_2"),
 *             complexHandler
 *         );
 *     }
 *
 *     // Handler với nhiều operators
 *     public static EventHandler createAdvancedHandler() {
 *         return upstream -> upstream
 *             .groupBy(EventAction::getType)
 *             .flatMap(group ->
 *                 group.buffer(5, TimeUnit.SECONDS, 10)
 *                      .map(events -> processEventBatch(events))
 *             )
 *             .map(result -> new EventAction<>("BATCH_PROCESSED", result))
 *             .doOnNext(event -> log.info("Batch processed: {}", event));
 *     }
 *
 *     // Handler với side effects
 *     public static EventHandler createLoggingHandler() {
 *         return upstream -> upstream
 *             .doOnNext(event -> log.info("Processing event: {}", event))
 *             .doOnError(error -> log.error("Error: ", error))
 *             .doFinally(() -> log.info("Handler completed"));
 *     }
 * }
 * }
 * </pre>
 */
public interface EventHandler extends ObservableTransformer<EventAction<?>, EventAction<?>> {
    @Override
    @NonNull
    Observable<EventAction<?>> apply(@NonNull Observable<EventAction<?>> upstream);
}

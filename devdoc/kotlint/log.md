```kotlin
logger.info(
    "Processing payment",
    kv(
        "payment",
        mapOf(
            "id" to 123,
            "amount" to 45,
        ),
    ),
)
logger.info("Processing order", kv("orderId", "aa"), kv("customerId", "bb"), kv("amount", "ccc"))
logger.info("Order {} processed for customer {}", value("orderId", "123"), value("customerId", "456"))
```

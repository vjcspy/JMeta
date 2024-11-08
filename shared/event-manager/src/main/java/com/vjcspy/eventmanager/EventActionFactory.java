package com.vjcspy.eventmanager;

/**
 * Ví dụ cách sử dụng EventActionFactory:
 * <p>
 * Đầu tiên, định nghĩa một payload type:
 *
 * <pre>
 * {@code
 * @Data
 * class UserPayload {
 *     private String name;
 *     private String email;
 * }
 * }
 * </pre>
 * <p>
 * Tiếp theo, sử dụng factory:
 *
 * <pre>
 * {@code
 * public class Example {
 *     // Định nghĩa action factory
 *     private static final ActionFactory<UserPayload> createUser =
 *         EventActionFactory.create("CREATE_USER");
 *
 *     public void example() {
 *         // Tạo payload
 *         UserPayload payload = new UserPayload();
 *         payload.setName("John");
 *         payload.setEmail("john@example.com");
 *
 *         // Tạo action
 *         EventAction<UserPayload> action = createUser.create(payload);
 *
 *         // Sử dụng action
 *         System.out.println(action.getType()); // In ra: "CREATE_USER"
 *         System.out.println(action.getPayload().getName()); // In ra: "John"
 *     }
 * }
 * }
 * </pre>
 */
public class EventActionFactory {
    public static <P> ActionFactory<P> create(String type) {
        return payload -> new EventAction<>(type, payload);
    }
}

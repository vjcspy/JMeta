/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.reactive.actions;

public interface StockSyncActions {}
//
// import com.vjcspy.eventmanager.ActionFactory;
// import com.vjcspy.eventmanager.EventActionFactory;
// import lombok.Builder;
// import lombok.Value;
//
// public final class StockSyncActions {
//    public static final String COR_LOAD_PAGE_ACTION = "COR_LOAD_NEXT_PAGE";
//    public static final String COR_LOAD_PAGE_SUCCESS_ACTION = "COR_LOAD_PAGE_SUCCESS";
//    public static final String COR_LOAD_PAGE_ERROR_ACTION = "COR_LOAD_PAGE_ERROR";
//
//    public final class Cor {
//        private Cor() {}
//
//        @Value
//        @Builder
//        public static class CorLoadPagePayload {
//            int page;
//        }
//
//        @Builder
//        @Value
//        public static class CorLoadPageSuccessPayload {
//            Object data;
//        }
//
//        @Value
//        @Builder
//        public static class CorLoadPageErrorPayload {
//            String error;
//        }
//
//        public static final ActionFactory<CorLoadPagePayload> corLoadPagePayloadActionFactory =
//                EventActionFactory.create(COR_LOAD_PAGE_ACTION);
//        public static final ActionFactory<CorLoadPageSuccessPayload> corLoadPagePayLoadActionSuccessFactory =
//                EventActionFactory.create(COR_LOAD_PAGE_SUCCESS_ACTION);
//        public static final ActionFactory<CorLoadPageErrorPayload> corLoadPageErrorPayloadActionFactory =
//                EventActionFactory.create(COR_LOAD_PAGE_ERROR_ACTION);
//    }
// }

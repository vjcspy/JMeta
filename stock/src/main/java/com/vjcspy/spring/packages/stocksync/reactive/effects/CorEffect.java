/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.reactive.effects;

import com.vjcspy.eventmanager.EventAction;
import com.vjcspy.eventmanager.EventHandler;
import com.vjcspy.spring.base.annotation.eventmanager.EventListener;
import com.vjcspy.spring.packages.stocksync.reactive.actions.StockSyncActions;
import com.vjcspy.spring.packages.stocksync.service.CorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CorEffect {
    private final CorService corService;

    public CorEffect(CorService corService) {
        this.corService = corService;
    }

    @EventListener(type = {StockSyncActions.COR_LOAD_PAGE_ACTION})
    public EventHandler handleUserEvents() {
        return upstream -> upstream.map((EventAction<?> event) -> {
                    StockSyncActions.Cor.CorLoadPagePayload payload =
                            (StockSyncActions.Cor.CorLoadPagePayload) event.getPayload();
                    return corService.getCorporateData(payload.getPage());
                })
                .map(stringObjectMap -> StockSyncActions.Cor.corLoadPagePayLoadActionSuccessFactory.create(
                        StockSyncActions.Cor.CorLoadPageSuccessPayload.builder()
                                .data(stringObjectMap)
                                .build()));
    }
}

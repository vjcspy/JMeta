package com.vjcspy.stockinfo.domain.tick;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TickService {
    private final TickRepository repository;

    @Inject
    public TickService(TickRepository repository) {
        this.repository = repository;
    }

    public Uni<List<List<String>>> getAvailableDate(String symbol) {
        return repository.find("symbol = ?1 order by date", symbol)
            .list()
            .onItem().transform(ticks -> {
                if (ticks.isEmpty()) {
                    return List.of();
                }

                List<List<String>> dateRanges = new ArrayList<>();
                LocalDate rangeStart = ticks.get(0).date;
                LocalDate rangeEnd = rangeStart;
                LocalDate prevDate = rangeStart;

                for (int i = 1; i < ticks.size(); i++) {
                    LocalDate currentDate = ticks.get(i).date;
                    if (currentDate.isAfter(prevDate.plusDays(1))) {
                        dateRanges.add(List.of(
                            rangeStart.toString(),
                            rangeEnd.toString()
                        ));
                        rangeStart = currentDate;
                    }
                    rangeEnd = currentDate;
                    prevDate = currentDate;
                }

                dateRanges.add(List.of(
                    rangeStart.toString(),
                    rangeEnd.toString()
                ));

                return dateRanges;
            });
    }
}

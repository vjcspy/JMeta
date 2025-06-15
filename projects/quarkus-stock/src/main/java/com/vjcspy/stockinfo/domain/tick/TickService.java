package com.vjcspy.stockinfo.domain.tick;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;


@ApplicationScoped
public class TickService {
    private final TickRepository repository;

    @Inject
    public TickService(TickRepository repository) {
        this.repository = repository;
    }

    public List<List<String>> getAvailableDate(String symbol) {
        // Get all ticks for the given symbol, sorted by date
        List<TickEntity> ticks = repository.find("symbol = ?1 order by date", symbol)
                .list()
                .await().indefinitely();

        if (ticks.isEmpty()) {
            return List.of();
        }

        // Process the results to identify continuous date ranges
        List<List<String>> dateRanges = new java.util.ArrayList<>();
        LocalDate rangeStart = ticks.get(0).date;
        LocalDate rangeEnd = rangeStart;
        LocalDate prevDate = rangeStart;

        for (int i = 1; i < ticks.size(); i++) {
            LocalDate currentDate = ticks.get(i).date;

            // Check if there's a gap in the dates
            if (currentDate.isAfter(prevDate.plusDays(1))) {
                // Add the completed range
                dateRanges.add(List.of(
                    rangeStart.toString(),
                    rangeEnd.toString()
                ));

                // Start a new range
                rangeStart = currentDate;
            }

            rangeEnd = currentDate;
            prevDate = currentDate;
        }

        // Add the last range
        dateRanges.add(List.of(
            rangeStart.toString(),
            rangeEnd.toString()
        ));

        return dateRanges;
    }
}

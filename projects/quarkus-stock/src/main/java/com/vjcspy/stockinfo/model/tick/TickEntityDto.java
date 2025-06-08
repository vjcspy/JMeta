package com.vjcspy.stockinfo.model.tick;


import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link TickEntity}
 */
public class TickEntityDto implements Serializable {
    private final Integer id;
    private final String symbol;
    private final LocalDate date;
    private final JsonNode meta;

    public TickEntityDto(Integer id, String symbol, LocalDate date, JsonNode meta) {
        this.id = id;
        this.symbol = symbol;
        this.date = date;
        this.meta = meta;
    }

    public Integer getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getDate() {
        return date;
    }

    public JsonNode getMeta() {
        return meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TickEntityDto entity = (TickEntityDto) o;
        return Objects.equals(this.id, entity.id) &&
            Objects.equals(this.symbol, entity.symbol) &&
            Objects.equals(this.date, entity.date) &&
            Objects.equals(this.meta, entity.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol, date, meta);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "symbol = " + symbol + ", " +
            "date = " + date + ", " +
            "meta = " + meta + ")";
    }
}

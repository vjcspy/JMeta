package com.vjcspy.stockinfo.domain.tick;

import com.fasterxml.jackson.databind.JsonNode;
import com.vjcspy.stockinfo.converter.JsonNodeConverter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "\"stock_info_ticks\"")
public class TickEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(length = 10, nullable = false)
    public String symbol;

    @Column(nullable = false)
    public LocalDate date;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonNodeConverter.class)
    public JsonNode meta;
}

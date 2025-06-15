package com.vjcspy.stockinfo.domain.tick;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link TickEntity}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TickDto implements Serializable {
    private Integer id;
    private String symbol;
    private LocalDate date;
    private JsonNode meta;
}

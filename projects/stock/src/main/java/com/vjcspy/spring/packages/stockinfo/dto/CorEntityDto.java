/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigInteger;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CorEntityDto {
    private Integer id;
    private Integer refId;
    private Integer catId;
    private String code;
    private String exchange;
    private String industryName1;
    private String industryName2;
    private String industryName3;
    private BigInteger totalShares;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate firstTradeDate;
}

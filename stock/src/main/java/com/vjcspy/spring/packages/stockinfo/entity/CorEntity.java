/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.entity;

import jakarta.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "cor_entity")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class CorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "cat_id")
    private Integer catId;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "industry_name1")
    private String industryName1;

    @Column(name = "industry_name2")
    private String industryName2;

    @Column(name = "industry_name3")
    private String industryName3;

    @Column(name = "total_shares")
    private BigInteger totalShares;

    @Column(name = "name")
    private String name;

    @Column(name = "first_trade_date")
    private LocalDate firstTradeDate;
}

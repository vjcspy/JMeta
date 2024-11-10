/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.entity;

import jakarta.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cor_entity")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "\"catId\"")
    private Integer catId;

    @Column(name = "\"refId\"")
    private Integer refId;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "\"industryName1\"")
    private String industryName1;

    @Column(name = "\"industryName2\"")
    private String industryName2;

    @Column(name = "\"industryName3\"")
    private String industryName3;

    @Column(name = "\"totalShares\"")
    private BigInteger totalShares;

    @Column(name = "name")
    private String name;

    @Column(name = "\"firstTradeDate\"")
    private LocalDate firstTradeDate;
}

package com.vjcspy.stockinfo.domain.cor;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "\"cor_entity\"")
public class CorEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    public Long id;

    @Column(name = "\"refId\"")
    public Integer refId;

    @Column(name = "\"catId\"")
    public Integer catId;

    @Column(name = "\"code\"", nullable = false, unique = true)
    public String code;

    @Column(name = "\"exchange\"", nullable = false)
    public String exchange;

    @Column(name = "\"industryName1\"")
    public String industryName1;

    @Column(name = "\"industryName2\"")
    public String industryName2;

    @Column(name = "\"industryName3\"")
    public String industryName3;

    @Column(name = "\"totalShares\"", nullable = false)
    public Long totalShares;

    @Column(name = "\"name\"", nullable = false)
    public String name;

    @Column(name = "\"firstTradeDate\"")
    public LocalDate firstTradeDate;
}

/* (C) 2024 */
package com.vjcspy.spring.packages.stockinfo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigInteger
import java.time.LocalDate

@Entity
@Table(name = "cor_entity")
class CorEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "\"catId\"")
    var catId: Int? = null,

    @Column(name = "\"refId\"")
    var refId: Int? = null,

    @Column(name = "code", unique = true)
    var code: String? = null,

    @Column(name = "exchange")
    var exchange: String? = null,

    @Column(name = "\"industryName1\"")
    var industryName1: String? = null,

    @Column(name = "\"industryName2\"")
    var industryName2: String? = null,

    @Column(name = "\"industryName3\"")
    var industryName3: String? = null,

    @Column(name = "\"totalShares\"")
    var totalShares: BigInteger? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "\"firstTradeDate\"")
    var firstTradeDate: LocalDate? = null,
)

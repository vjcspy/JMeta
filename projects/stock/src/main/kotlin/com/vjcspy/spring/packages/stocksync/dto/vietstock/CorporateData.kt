// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.dto.vietstock

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class CorporateData(
    val ID: Int,
    val CatID: Int,
    val Exchange: String,
    val IndustryName1: String,
    val IndustryName2: String,
    val IndustryName3: String,
    val Code: String,
    val Name: String,
    val TotalShares: Long,
    val URL: String,
    val Row: Int,
    @Serializable(with = DateDeserializer::class)
    val FirstTradeDate: LocalDate,
    val TotalRecord: Int,
)

object DateDeserializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        val rawDate = decoder.decodeString() // Lấy chuỗi gốc: "/Date(1540227600000)/"
        val epochMillis = rawDate.substringAfter("(").substringBefore(")").toLong() // Lấy giá trị "1540227600000"

        // Áp dụng múi giờ UTC (UTC+0)
        return Instant
            .ofEpochMilli(epochMillis)
            .atZone(ZoneId.of("UTC"))
            .toLocalDate()
    }

    override fun serialize(
        encoder: Encoder,
        value: LocalDate,
    ) {
        // Chuyển LocalDate về thời điểm 00:00 theo UTC
        val epochMillis =
            value
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()
                .toEpochMilli()

        encoder.encodeString("/Date($epochMillis)/") // Format lại thành chuỗi JSON
    }
}

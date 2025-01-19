package com.vjcspy.spring.base.splunk

import com.fasterxml.jackson.databind.ObjectMapper
import com.vjcspy.kotlinutilities.log.KtLogging
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.exists
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class FailedSplunkEventsProcessor(
    private val objectMapper: ObjectMapper,
    @Value("\${splunk.hec.url}") private val splunkUrl: String,
    @Value("\${splunk.hec.token}") private val splunkToken: String,
    @Value("\${logging.file.path:./logs}") private val logsPath: String,
) {
    private val logger = KtLogging.logger {}
    private val batchSize = 100
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")

    // Create RestTemplate directly in the class
    private val restTemplate: RestTemplate =
        RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10))
            .build()

    @Scheduled(fixedDelay = 300000) // Run every 5 minutes
    fun processFailedEvents() {
        try {
            val failedEventsPath = Paths.get(logsPath, "failed-splunk-events.log")
            if (!failedEventsPath.exists()) {
                logger.debug("No failed events file found at: $failedEventsPath")
                return
            }

            processEventsFile(failedEventsPath)
        } catch (e: Exception) {
            logger.error("Error processing failed Splunk events", e)
        }
    }

    private fun processEventsFile(filePath: Path) {
        // Read all events
        val events = Files.readAllLines(filePath)
        if (events.isEmpty()) {
            logger.debug("No events found in file")
            return
        }

        // Process events in batches
        events.chunked(batchSize).forEach { batch ->
            try {
                sendToSplunk(batch)
            } catch (e: Exception) {
                logger.error("Failed to process batch of ${batch.size} events", e)
                // Save failed batch to a new file
                saveFailedBatch(batch)
            }
        }

        // Backup and clear original file
        backupAndClearFile(filePath)
    }

    private fun sendToSplunk(events: List<String>) {
        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
                set("Authorization", "Splunk $splunkToken")
            }

        events.forEach { event ->
            try {
                // Parse the event to ensure it's valid JSON
                val eventJson = objectMapper.readTree(event)

                restTemplate.postForEntity(
                    splunkUrl,
                    eventJson,
                    String::class.java,
                )
            } catch (e: Exception) {
                logger.error("Failed to send event to Splunk: $event", e)
                throw e
            }
        }
    }

    private fun saveFailedBatch(events: List<String>) {
        try {
            val timestamp = LocalDateTime.now().format(dateFormatter)
            val failedBatchPath = Paths.get(logsPath, "failed-batch-$timestamp.log")

            Files.write(failedBatchPath, events)
            logger.info("Saved failed batch to: $failedBatchPath")
        } catch (e: Exception) {
            logger.error("Failed to save failed batch", e)
        }
    }

    private fun backupAndClearFile(originalFile: Path) {
        try {
            val timestamp = LocalDateTime.now().format(dateFormatter)
            val backupPath =
                Paths.get(
                    logsPath,
                    "archive",
                    "failed-splunk-events-$timestamp.log",
                )

            // Create archive directory if it doesn't exist
            Files.createDirectories(backupPath.parent)

            // Move the original file to backup
            Files.move(
                originalFile,
                backupPath,
                StandardCopyOption.REPLACE_EXISTING,
            )

            logger.info("Backed up processed events to: $backupPath")
        } catch (e: Exception) {
            logger.error("Failed to backup and clear file", e)
            throw e
        }
    }

    /**
     * Clean up old backup files
     * Runs once a day
     */
    @Scheduled(cron = "0 0 0 * * ?")
    fun cleanupOldBackups() {
        try {
            val archivePath = Paths.get(logsPath, "archive")
            if (!archivePath.exists()) return

            // Keep last 7 days of backups
            val retentionDays = 7L
            val now = LocalDateTime.now()

            Files.list(archivePath).forEach { file ->
                try {
                    val fileName = file.fileName.toString()
                    // Extract date from filename
                    val dateStr = fileName.substringAfterLast("-").substringBefore(".")
                    val fileDate = LocalDateTime.parse(dateStr, dateFormatter)

                    if (fileDate.plusDays(retentionDays).isBefore(now)) {
                        Files.delete(file)
                        logger.info("Deleted old backup file: $fileName")
                    }
                } catch (e: Exception) {
                    logger.error("Failed to process backup file: ${file.fileName}", e)
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to clean up old backups", e)
        }
    }
}

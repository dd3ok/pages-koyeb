package com.dd3ok.pageskoyeb.config

import com.dd3ok.pageskoyeb.repository.home.HomeContactEntity
import com.dd3ok.pageskoyeb.repository.wedding.WeddingCommentEntity
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.Index
import org.springframework.stereotype.Component

@Component
class MongoIndexInitializer(
    private val mongoTemplate: MongoTemplate
) {

    private val logger = LoggerFactory.getLogger(MongoIndexInitializer::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun ensureIndexesAfterStartup() {
        Thread({
            ensureIndexes()
        }, "mongo-index-initializer").apply {
            isDaemon = true
            start()
        }
    }

    fun ensureIndexes() {
        try {
            mongoTemplate.indexOps(HomeContactEntity::class.java)
                .createIndex(Index().on("created_at", Sort.Direction.DESC))
            mongoTemplate.indexOps(HomeContactEntity::class.java)
                .createIndex(Index().on("email", Sort.Direction.ASC))
            mongoTemplate.indexOps(WeddingCommentEntity::class.java)
                .createIndex(Index().on("created_at", Sort.Direction.DESC))
        } catch (exception: Exception) {
            logger.warn("Mongo index creation skipped: {}", exception.message)
        }
    }
}

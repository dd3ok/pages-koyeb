package com.dd3ok.pageskoyeb.config

import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Configuration
class MongoConnectionPoolConfig {

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun mongoMaxPoolSizeCustomizer(): MongoClientSettingsBuilderCustomizer =
        MongoClientSettingsBuilderCustomizer { builder ->
            builder.applyToConnectionPoolSettings { settings ->
                settings.maxSize(10)
            }
        }
}

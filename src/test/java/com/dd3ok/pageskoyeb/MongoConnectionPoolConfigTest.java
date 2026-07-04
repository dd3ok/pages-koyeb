package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;

import com.dd3ok.pageskoyeb.config.MongoConnectionPoolConfig;
import com.mongodb.MongoClientSettings;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;

class MongoConnectionPoolConfigTest {

    @Test
    void limitsMongoConnectionPoolToTenConnections() {
        MongoClientSettingsBuilderCustomizer customizer =
                new MongoConnectionPoolConfig().mongoMaxPoolSizeCustomizer();

        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        customizer.customize(builder);

        assertThat(builder.build().getConnectionPoolSettings().getMaxSize()).isEqualTo(10);
    }
}

package com.grocery.backend.config;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.DownloadConfigBuilder;
import de.flapdoodle.embed.process.config.store.HttpProxyFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@ConditionalOnProperty(name = "app.embeddedMongo", havingValue = "true", matchIfMissing = true)
public class EmbeddedMongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        // Use local connection string; flapdoodle auto-starts embedded server behind the scenes in recent versions
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/grocery"));
    }
}


package com.example.BookManager.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConditionalOnProperty(prefix = "app.cache")
@ConfigurationProperties
public class AppProperties {

    private final List<String> cacheNames = new ArrayList<>();

    private final Map<String, CacheProperties> caches = new HashMap<>();

    private final String cacheType = "redis";
    @Data
    public static class CacheProperties {
        private final Duration expiry = Duration.ZERO;
    }

    interface Caches {
        String ENTITIES_BY_CATEGORY = "entitiesByCategory";
        String ENTITY_BY_BOOK_NAME_AND_AUTHOR_NAME = "entityByBookNameAndBookAuthor";
    }
}

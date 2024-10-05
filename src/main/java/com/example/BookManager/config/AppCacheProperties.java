package com.example.BookManager.config;

import lombok.Data;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConditionalOnBean(CacheConfig.class)
@ConfigurationProperties(prefix = "app.cache")
public class AppCacheProperties {

    private List<String> cacheNames = new ArrayList<>();

    private Map<String, CacheProperties> caches = new HashMap<>();

    private String cacheType = "";
    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;
    }

    public interface CacheNames {
        String ENTITIES_BY_CATEGORY = "entitiesByCategory";
        String ENTITY_BY_BOOK_NAME_AND_AUTHOR_NAME = "entityByBookNameAndBookAuthor";
    }
}

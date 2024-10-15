package com.demo.bbq.commons.cache;

import static com.demo.bbq.commons.cache.enums.CleaningFrequency.FIVE_MINUTES;
import static com.demo.bbq.commons.cache.enums.CleaningFrequency.getTimeToLive;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@RequiredArgsConstructor
public class CustomRedisCacheManager implements CacheManager {

  private final RedisConnectionFactory redisConnectionFactory;
  private final ConfigurationBaseProperties properties;
  private RedisCacheManager redisCacheManager;

  @PostConstruct
  public void init() {
    redisCacheManager = createRedisCacheManager();
  }

  @Override
  public Cache getCache(String name) {
    return isRedisAvailable()
        ? redisCacheManager.getCache(name)
        : new ConcurrentMapCache(name);
  }

  @Override
  public Collection<String> getCacheNames() {
    return redisCacheManager.getCacheNames();
  }

  private RedisCacheManager createRedisCacheManager() {
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
    properties.getCache().forEach((key, value) ->
        cacheConfigurations.put(key, RedisCacheConfiguration.defaultCacheConfig().entryTtl(getTimeToLive(value))));
    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(FIVE_MINUTES.getTimeToLive());

    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(defaultConfig)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
  }

  public boolean isRedisAvailable() {
    try {
      redisConnectionFactory.getConnection().ping();
      return true;
    } catch (Exception exception) {
      return false;
    }
  }
}
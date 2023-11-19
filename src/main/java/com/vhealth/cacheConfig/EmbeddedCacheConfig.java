package com.vhealth.cacheConfig;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;

@Configuration
@EnableCaching
class EmbeddedCacheConfig {

  @Bean
  Config config() {
    Config config = new Config();

    MapConfig mapConfig = new MapConfig();
    System.out.println("EmbeddedCacheConfig.config()");
    mapConfig.setTimeToLiveSeconds(3);
    config.getMapConfigs().put("repo", mapConfig);

    return config;
  }
}
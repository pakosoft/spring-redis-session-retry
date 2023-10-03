package com.pako.example.redissessionretry.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.data.redis.ReactiveRedisSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
@EnableRedisWebSession
public class RedisSessionConfiguration {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(RedisSessionConfiguration.class);

    @Bean
    public Retry retrySpec(@Value("${redis.retry.maxAttempts}") int maxAttempts,
                           @Value("${redis.retry.backoffDelayMillis}") long backoffDelayMillis) {
        return Retry.backoff(maxAttempts, Duration.ofMillis(backoffDelayMillis))
                .doBeforeRetry(retrySignal ->
                        logger.warn("Retrying attempt {} due to '{}'", retrySignal.totalRetries()+1, retrySignal.failure().getMessage()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            logger.error("Retries exhausted after {} with {}", retrySignal.totalRetries(), retrySignal.failure());
                            return retrySignal.failure();
                        }
                );
    }
    @Bean
    @Primary
    @ConditionalOnProperty(name = "redis.retry.enabled", havingValue = "true")
    public ReactiveSessionRepository reactiveRedisSessionRepository(
            ReactiveRedisSessionRepository redisSessionRepository,
            Retry retrySpec) {
        return new ReactiveRetrySessionRepository(redisSessionRepository, retrySpec);
    }

}

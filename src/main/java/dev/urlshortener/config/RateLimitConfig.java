
package dev.urlshortener.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    private final ConcurrentHashMap<String, Bucket> redirectBuckets = new ConcurrentHashMap<>();

    @Bean
    public Bucket createLinkBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(20)
                .refillIntervally(20, Duration.ofMinutes(1))
                .build();

        return Bucket.builder().addLimit(limit).build();
    }

    public Bucket resolveRedirectBucket(String ipAddress) {
        return redirectBuckets.computeIfAbsent(ipAddress, ip -> {
            Bandwidth limit = Bandwidth.builder()
                    .capacity(300)
                    .refillIntervally(300, Duration.ofMinutes(1))
                    .build();

            return Bucket.builder().addLimit(limit).build();
        });
    }
}

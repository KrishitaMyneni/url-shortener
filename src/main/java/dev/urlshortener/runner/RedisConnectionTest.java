package dev.urlshortener.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisConnectionTest implements CommandLineRunner {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisConnectionTest(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) {

        redisTemplate.opsForValue().set("connection-test", "Redis is working!");

        String result = redisTemplate.opsForValue().get("connection-test");

        System.out.println("REDIS TEST: " + result);
    }
}


package dev.urlshortener.controller;

import dev.urlshortener.entity.ClickDaily;
import dev.urlshortener.entity.Link;
import dev.urlshortener.repository.ClickDailyRepository;
import dev.urlshortener.repository.LinkRepository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;

@RestController
public class RedirectController {

    private final LinkRepository linkRepository;
    private final ClickDailyRepository clickDailyRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public RedirectController(
            LinkRepository linkRepository,
            ClickDailyRepository clickDailyRepository,
            RedisTemplate<String, String> redisTemplate) {

        this.linkRepository = linkRepository;
        this.clickDailyRepository = clickDailyRepository;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        String originalUrl = redisTemplate.opsForValue().get(code);

        Link link;

        if (originalUrl == null) {

            link = linkRepository.findByCode(code)
                    .orElseThrow(() -> new RuntimeException("Short URL not found"));

            originalUrl = link.getOriginalUrl();

            redisTemplate.opsForValue().set(
                    code,
                    originalUrl,
                    Duration.ofHours(24));

        } else {

            link = linkRepository.findByCode(code)
                    .orElseThrow(() -> new RuntimeException("Short URL not found"));
        }

        link.setClickCount(link.getClickCount() + 1);
        linkRepository.save(link);

        LocalDate today = LocalDate.now();

        ClickDaily dailyClick = clickDailyRepository
                .findByLinkIdAndClickDate(link.getId(), today)
                .orElseGet(() -> {
                    ClickDaily newClick = new ClickDaily();
                    newClick.setLink(link);
                    newClick.setClickDate(today);
                    newClick.setClickCount(0L);
                    return newClick;
                });

        dailyClick.setClickCount(dailyClick.getClickCount() + 1);
        clickDailyRepository.save(dailyClick);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}

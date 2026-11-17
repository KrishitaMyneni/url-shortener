package dev.urlshortener.controller;

import dev.urlshortener.entity.ClickDaily;
import dev.urlshortener.entity.Link;
import dev.urlshortener.repository.ClickDailyRepository;
import dev.urlshortener.repository.LinkRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/links")
public class DailyStatsController {

    private final LinkRepository linkRepository;
    private final ClickDailyRepository clickDailyRepository;

    public DailyStatsController(
            LinkRepository linkRepository,
            ClickDailyRepository clickDailyRepository) {
        this.linkRepository = linkRepository;
        this.clickDailyRepository = clickDailyRepository;
    }

    @GetMapping("/{code}/daily-stats")
    public ResponseEntity<?> getDailyStats(@PathVariable String code) {

        Link link = linkRepository.findByCode(code).orElse(null);

        if (link == null) {
            return ResponseEntity.notFound().build();
        }

        List<ClickDaily> dailyClicks = clickDailyRepository.findByLinkIdOrderByClickDateAsc(link.getId());

        List<Map<String, Object>> stats = dailyClicks.stream()
                .map(click -> Map.<String, Object>of(
                        "date", click.getClickDate(),
                        "clickCount", click.getClickCount()))
                .toList();

        return ResponseEntity.ok(stats);
    }
}
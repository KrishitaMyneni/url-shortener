package dev.urlshortener.controller;

import dev.urlshortener.entity.Link;
import dev.urlshortener.repository.LinkRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/links")
public class StatsController {

    private final LinkRepository linkRepository;

    public StatsController(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @GetMapping("/{code}/stats")
    public ResponseEntity<?> getStats(@PathVariable String code) {

        Link link = linkRepository.findByCode(code)
                .orElse(null);

        if (link == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(
                "code", link.getCode(),
                "originalUrl", link.getOriginalUrl(),
                "clickCount", link.getClickCount(),
                "createdAt", link.getCreatedAt()));
    }
}
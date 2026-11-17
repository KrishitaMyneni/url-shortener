package dev.urlshortener.controller;

import dev.urlshortener.entity.Link;
import dev.urlshortener.service.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public ResponseEntity<?> createShortLink(@RequestBody Map<String, String> request) {

        String originalUrl = request.get("url");

        if (originalUrl == null || originalUrl.isBlank()) {
            return ResponseEntity.badRequest().body("URL is required");
        }

        Link link = linkService.createShortLink(originalUrl);

        return ResponseEntity.ok(Map.of(
                "code", link.getCode(),
                "originalUrl", link.getOriginalUrl(),
                "shortUrl", "http://localhost:8080/" + link.getCode()));
    }
}
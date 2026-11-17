package dev.urlshortener.controller;

import dev.urlshortener.entity.Link;
import dev.urlshortener.repository.LinkRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class TopLinksController {

    private final LinkRepository linkRepository;

    public TopLinksController(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @GetMapping("/top")
    public List<Map<String, Object>> getTopLinks(
            @RequestParam(defaultValue = "10") int limit) {

        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("Limit must be between 1 and 100");
        }

        return linkRepository
                .findAllByOrderByClickCountDesc(PageRequest.of(0, limit))
                .stream()
                .map(link -> Map.<String, Object>of(
                        "code", link.getCode(),
                        "originalUrl", link.getOriginalUrl(),
                        "clickCount", link.getClickCount()))
                .toList();
    }
}
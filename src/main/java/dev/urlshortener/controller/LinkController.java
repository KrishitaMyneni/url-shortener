package dev.urlshortener.controller;

import dev.urlshortener.dto.CreateLinkRequest;
import dev.urlshortener.entity.Link;
import dev.urlshortener.service.LinkService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> createShortLink(
            @Valid @RequestBody CreateLinkRequest request) {

        Link link = linkService.createShortLink(request.url(), request.customCode());

        return ResponseEntity.ok(Map.of(
                "code", link.getCode(),
                "originalUrl", link.getOriginalUrl(),
                "shortUrl", "http://localhost:8080/" + link.getCode()));
    }
}
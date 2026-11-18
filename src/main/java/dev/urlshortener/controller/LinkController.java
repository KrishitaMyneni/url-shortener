
package dev.urlshortener.controller;

import dev.urlshortener.config.RateLimitConfig;
import dev.urlshortener.dto.CreateLinkRequest;
import dev.urlshortener.entity.Link;
import dev.urlshortener.service.LinkService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkService linkService;
    private final RateLimitConfig rateLimitConfig;

    public LinkController(
            LinkService linkService,
            RateLimitConfig rateLimitConfig) {
        this.linkService = linkService;
        this.rateLimitConfig = rateLimitConfig;
    }

    @PostMapping
    public ResponseEntity<?> createShortLink(
            @Valid @RequestBody CreateLinkRequest request,
            HttpServletRequest httpRequest) {

        String ipAddress = httpRequest.getRemoteAddr();
        Bucket bucket = rateLimitConfig.resolveCreateBucket(ipAddress);

        if (!bucket.tryConsume(1)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Rate limit exceeded. Try again later."));
        }

        Link link = linkService.createShortLink(
                request.url(), request.customCode());

        return ResponseEntity.ok(Map.of(
                "code", link.getCode(),
                "originalUrl", link.getOriginalUrl(),
                "shortUrl", "http://localhost:8080/" + link.getCode()));
    }
}

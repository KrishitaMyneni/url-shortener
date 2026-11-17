package dev.urlshortener.controller;

import dev.urlshortener.entity.Link;
import dev.urlshortener.repository.LinkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class RedirectController {

    private final LinkRepository linkRepository;

    public RedirectController(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        Link link = linkRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(link.getOriginalUrl()))
                .build();
    }
}
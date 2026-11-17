package dev.urlshortener.controller;

import dev.urlshortener.entity.ClickDaily;
import dev.urlshortener.entity.Link;
import dev.urlshortener.repository.ClickDailyRepository;
import dev.urlshortener.repository.LinkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
public class RedirectController {

    private final LinkRepository linkRepository;
    private final ClickDailyRepository clickDailyRepository;

    public RedirectController(
            LinkRepository linkRepository,
            ClickDailyRepository clickDailyRepository) {
        this.linkRepository = linkRepository;
        this.clickDailyRepository = clickDailyRepository;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        Link link = linkRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

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
                .location(URI.create(link.getOriginalUrl()))
                .build();
    }
}
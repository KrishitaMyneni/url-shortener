
package dev.urlshortener.service;

import dev.urlshortener.entity.Link;
import dev.urlshortener.repository.LinkRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Link createShortLink(String originalUrl, String customCode) {

        validateUrl(originalUrl);

        String code;

        if (customCode != null && !customCode.isBlank()) {

            if (linkRepository.existsByCode(customCode)) {
                throw new IllegalArgumentException("Custom code is already taken");
            }

            code = customCode;

        } else {

            do {
                code = generateCode();
            } while (linkRepository.existsByCode(code));
        }

        Link link = new Link();
        link.setCode(code);
        link.setOriginalUrl(originalUrl);

        return linkRepository.save(link);
    }

    private String generateCode() {

        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    private void validateUrl(String url) {

        try {
            URI uri = new URI(url);

            String scheme = uri.getScheme();

            if (scheme == null ||
                    (!scheme.equalsIgnoreCase("http") &&
                            !scheme.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException(
                        "Only HTTP and HTTPS URLs are allowed");
            }

            if (uri.getHost() == null || uri.getHost().isBlank()) {
                throw new IllegalArgumentException("Invalid URL host");
            }

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}

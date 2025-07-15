package com.jonathanj00.url_shortener.controller;

import com.jonathanj00.url_shortener.dto.ShortenRequest;
import com.jonathanj00.url_shortener.dto.ShortenResponse;
import com.jonathanj00.url_shortener.dto.UrlsResponse;
import com.jonathanj00.url_shortener.service.UrlShortenerService;
import com.jonathanj00.url_shortener.utility.RequestUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

import static com.jonathanj00.url_shortener.utility.RequestUtility.getBaseUrl;

/**
 * Controller defining RESTful interface for application.
 * Please see openapi.yaml in projects root directory for API specification.
 */
@RestController
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody ShortenRequest request, HttpServletRequest httpRequest) {
        try {
            String alias = urlShortenerService.shortenUrl(request.getFullUrl(), request.getCustomAlias());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ShortenResponse(getBaseUrl(httpRequest) + alias));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{alias}")
    public RedirectView redirectFullUrl(@PathVariable String alias) {
        String fullUrl = urlShortenerService.getFullUrl(alias);

        if (fullUrl == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alias not found");
        }

        RedirectView redirectView = new RedirectView(fullUrl);
        redirectView.setStatusCode(HttpStatus.FOUND); // 302 status
        return redirectView;
    }

    @DeleteMapping("/{alias}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlias(@PathVariable String alias) {
        try {
            urlShortenerService.deleteUrl(alias);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alias not found");
        }
    }

    @GetMapping("/urls")
    public List<UrlsResponse> getAllUrls(HttpServletRequest httpRequest) {
        return urlShortenerService.getAllUrls().stream()
                .map(entry -> new UrlsResponse(
                        entry.getAlias(),
                        entry.getUrl(),
                        RequestUtility.getBaseUrl(httpRequest) + entry.getAlias()
                ))
                .collect(Collectors.toList());
    }
}

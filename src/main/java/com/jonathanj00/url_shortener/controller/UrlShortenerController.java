package com.jonathanj00.url_shortener.controller;

import com.jonathanj00.url_shortener.dto.ShortenRequest;
import com.jonathanj00.url_shortener.dto.ShortenResponse;
import com.jonathanj00.url_shortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.jonathanj00.url_shortener.utility.RequestUtility.getBaseUrl;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService urlService;

    public UrlShortenerController(UrlShortenerService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody ShortenRequest request, HttpServletRequest httpRequest) {
        try {
            String alias = urlService.shortenUrl(request.getFullUrl(), request.getCustomAlias());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ShortenResponse(getBaseUrl(httpRequest) + alias));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

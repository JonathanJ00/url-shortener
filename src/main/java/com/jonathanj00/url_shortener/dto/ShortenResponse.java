package com.jonathanj00.url_shortener.dto;

/**
 * DTO class defining response body of requests to /shorten interface method.
 */
public class ShortenResponse {
    private String alias;

    public ShortenResponse(String shortUrl) {
        this.alias = shortUrl;
    }

    public String getShortUrl() {
        return alias;
    }

    public void setShortUrl(String shortUrl) {
        this.alias = shortUrl;
    }
}

package com.jonathanj00.url_shortener.dto;

/**
 * DTO class defining response body containing details of a url entity.
 */
public class UrlsResponse {
    private String alias;
    private String fullUrl;
    private String shortUrl;

    public UrlsResponse(String alias, String fullUrl, String shortUrl) {
        this.alias = alias;
        this.fullUrl = fullUrl;
        this.shortUrl = shortUrl;
    }

    public String getAlias() {
        return alias;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}

package com.jonathanj00.url_shortener.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

/**
 * DTO class defining request body of requests to /shorten interface method.
 */
public class ShortenRequest {
    @URL(message = "Invalid URL format")
    @NotNull
    private String fullUrl;

    private String customAlias;

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }
}

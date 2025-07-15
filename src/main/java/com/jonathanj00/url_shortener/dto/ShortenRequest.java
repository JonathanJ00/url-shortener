package com.jonathanj00.url_shortener.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

/**
 * DTO class defining request body of requests to /shorten interface method.
 */
public class ShortenRequest {
    @URL(message = "Invalid URL format")
    @NotNull
    private String fullUrl;

    @Pattern(regexp = "^[A-Za-z0-9]*$")
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

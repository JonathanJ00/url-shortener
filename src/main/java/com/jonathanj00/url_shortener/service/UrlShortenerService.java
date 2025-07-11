package com.jonathanj00.url_shortener.service;

import com.jonathanj00.url_shortener.entity.Url;
import com.jonathanj00.url_shortener.repository.UrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlShortenerService {

    private final UrlRepository urlRepository;

    public UrlShortenerService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    /**
     * Generates an alias for the provided URL and saves this URL and alias pair to the database.
     * If URL is already present in database then returns existing alias instead.
     *
     * @param url
     * @return Alias for the provided URL.
     */
    public String shorten(String url) {
        if (urlRepository.existsByUrl(url)) {
            List<Url> existingUrls = urlRepository.findByUrl(url);
            return existingUrls.getFirst().getAlias();
        }

        String alias = generateRandomAlias();

        Url newUrl = new Url(url, alias);
        urlRepository.save(newUrl);

        return alias;
    }

    private String generateRandomAlias() {
        int stringLength = 6;
        String alias = RandomStringUtils.randomAlphanumeric(stringLength);
        alias = alias.toLowerCase();

        while (urlRepository.existsByAlias(alias)) {
            stringLength = +1;
            alias = RandomStringUtils.randomAlphanumeric(stringLength);
        }

        return alias;
    }
}

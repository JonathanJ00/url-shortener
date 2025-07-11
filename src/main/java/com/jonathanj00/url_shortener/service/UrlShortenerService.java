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
     * Saves the url against an alias. If no custom alias is provided then one will be generated.
     *
     * @param fullUrl
     * @param customAlias
     * @return
     */
    public String shortenUrl(String fullUrl, String customAlias) {
        if (customAlias != null) {
            shortenUrlAliasProvided(fullUrl, customAlias);
            return customAlias;
        }

        return shortenNewAlias(fullUrl);
    }

    /**
     * Checks that neither the provided url or alias exist in the db and saves them if not.
     *
     * @param url
     * @param alias
     * @throws IllegalArgumentException If either the URL or alias is already present in the db.
     */
    public void shortenUrlAliasProvided(String url, String alias) throws IllegalArgumentException {
        if (urlRepository.existsByAlias(alias)) {
            throw new IllegalArgumentException("Alias already exists.");
        }

        if (urlRepository.existsByUrl(url)) {
            throw new IllegalArgumentException("Url already shortened.");
        }

        urlRepository.save(new Url(url, alias));
    }

    /**
     * Generates an alias for the provided URL and saves this URL and alias pair to the database.
     * If URL is already present in database then returns existing alias instead.
     *
     * @param url
     * @return Alias for the provided URL.
     */
    public String shortenNewAlias(String url) {
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

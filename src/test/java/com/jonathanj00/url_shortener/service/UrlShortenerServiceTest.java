package com.jonathanj00.url_shortener.service;

import com.jonathanj00.url_shortener.entity.Url;
import com.jonathanj00.url_shortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class UrlShortenerServiceTest {

    @Mock
    UrlRepository urlRepository;

    @InjectMocks
    UrlShortenerService urlShortenerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShorten_NewAlias_shouldReturnValidAlias() {
        String url = "www.example.com";
        Url urlEntity = new Url();

        when(urlRepository.existsByUrl(url)).thenReturn(false);
        when(urlRepository.save(ArgumentMatchers.any())).thenReturn(urlEntity);


        String alias = urlShortenerService.shortenNewAlias(url);
        assertEquals(6, alias.length(), "Incorrect alias length");
    }

    @Test
    public void testShorten_NewAlias_ifUrlExists_shouldReturnExistingAlias() {
        String url = "www.example.com";
        String alias = "alias";
        Url urlEntity = new Url(url, alias);

        when(urlRepository.existsByUrl(url)).thenReturn(true);
        when(urlRepository.findByUrl(url)).thenReturn(List.of(urlEntity));

        String returnAlias = urlShortenerService.shortenNewAlias(url);
        assertEquals(alias, returnAlias, "Should return existing alias");
    }

    @Test
    public void testShortenProvidedAlias_shouldReturnAlias() {
        String url = "www.example.com";
        String alias = "alias";
        Url urlEntity = new Url(url, alias);

        when(urlRepository.existsByUrl(url)).thenReturn(false);
        when(urlRepository.existsByAlias(alias)).thenReturn(false);
        when(urlRepository.save(ArgumentMatchers.any())).thenReturn(urlEntity);

        String returnAlias = urlShortenerService.shortenUrl(url, alias);
        assertEquals(alias, returnAlias, "Should return existing alias");
    }

    @Test
    public void testShortenProvidedAlias_ifUrlExists_shouldThrowError() {
        String url = "www.example.com";
        String alias = "alias";

        when(urlRepository.existsByUrl(url)).thenReturn(true);

        assertThatThrownBy(() -> urlShortenerService.shortenUrlAliasProvided(url, alias)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testShortenProvidedAlias_ifAliasExists_shouldThrowError() {
        String url = "www.example.com";
        String alias = "alias";

        when(urlRepository.existsByUrl(url)).thenReturn(false);
        when(urlRepository.existsByAlias(alias)).thenReturn(true);

        assertThatThrownBy(() -> urlShortenerService.shortenUrlAliasProvided(url, alias)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGetFullUrl_shouldReturnValidUrl() {
        String url = "www.example.com";
        String alias = "alias";
        Url urlEntity = new Url(url, alias);

        when(urlRepository.findByAlias(alias)).thenReturn(List.of(urlEntity));

        assertEquals(url, urlShortenerService.getFullUrl(alias), "Incorrect full url");
    }

    @Test
    public void testGetFullUrl_noAliasFound_shouldReturnNull() {
        String alias = "alias";

        when(urlRepository.findByAlias(alias)).thenReturn(new ArrayList<>());

        assertNull(urlShortenerService.getFullUrl(alias), "Expected response of null");
    }

    @Test
    public void testDeleteUrl_aliasNotPresent_shouldThrowException() {
        String alias = "alias";

        when(urlRepository.findByAlias(alias)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> urlShortenerService.deleteUrl(alias)).isInstanceOf(IllegalArgumentException.class);
    }
}

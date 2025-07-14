package com.jonathanj00.url_shortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathanj00.url_shortener.dto.ShortenRequest;
import com.jonathanj00.url_shortener.entity.Url;
import com.jonathanj00.url_shortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UrlShortenerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String alias1 = "alias1";
    private final String alias2 = "alias2";
    private final String url1 = "url1";
    private final String url2 = "url2";

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        repository.save(new Url(url1, alias1));
        repository.save(new Url(url2, alias2));
        repository.flush();
    }

    @Test
    public void getAllUrls_shouldReturnDataFromDatabase() throws Exception {
        mockMvc.perform(get("/urls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.alias == '" + alias1 + "')].fullUrl").value(url1))
                .andExpect(jsonPath("$[?(@.alias == '" + alias2 + "')].fullUrl").value(url2));
    }

    @Test
    public void shortenUrl_shouldAddDataToDatabase() throws Exception {
        String newAlias = "newAlias";
        String newUrl = "newUrl";
        ShortenRequest request = new ShortenRequest();
        request.setFullUrl(newUrl);
        request.setCustomAlias(newAlias);

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        List<Url> savedUrls = repository.findByUrl(newUrl);

        Url expectedUrl = new Url(newUrl, newAlias);
        assertEquals(expectedUrl, savedUrls.getFirst(), "The saved url does not have the expected details.");
    }

    @Test
    public void shortenUrl_urlAlreadyExists_shouldNotAddDataToDatabase() throws Exception {
        ShortenRequest request = new ShortenRequest();
        request.setFullUrl(url1);

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value("http://localhost/" + alias1));

        List<Url> savedUrls = repository.findAll();
        assertEquals(2, savedUrls.size(), "Database should not have changed.");
    }

    @Test
    public void shortenUrl_aliasAlreadyExists_shouldNotAddDataToDatabase() throws Exception {
        String newUrl = "newUrl";
        ShortenRequest request = new ShortenRequest();
        request.setFullUrl(newUrl);
        request.setCustomAlias(alias1);

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Alias already exists")));

        assertFalse(repository.existsByUrl(newUrl), "Data should not have been added.");
    }

    @Test
    public void redirectFullUrl_shouldRedirectToFullUrl() throws Exception {
        mockMvc.perform(get("/" + alias1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(url1));
    }

    @Test
    public void redirectFullUrl_invalidAlias_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("Alias not found")));
    }

    @Test
    public void deleteAlias_shouldDeleteDataFromDatabase() throws Exception {
        mockMvc.perform(delete("/" + alias1))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsByAlias(alias1));
    }

    @Test
    public void deleteAlias_aliasDoesNotExist_shouldNotDeleteDataFromDatabase() throws Exception {
        mockMvc.perform(delete("/test"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("Alias not found")));

        List<Url> savedUrls = repository.findAll();
        assertEquals(2, savedUrls.size(), "Database should not have changed.");
    }
}

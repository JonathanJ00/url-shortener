package com.jonathanj00.url_shortener.controller;

import com.jonathanj00.url_shortener.dto.UrlsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class UrlViewController {

    private final RestTemplate restTemplate;

    @Autowired
    public UrlViewController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/view-urls")
    public String viewUrls(Model model) {
        String apiUrl = "http://localhost:8080/urls";

        ResponseEntity<UrlsResponse[]> response = restTemplate.getForEntity(apiUrl, UrlsResponse[].class);

        List<UrlsResponse> urlsList = Arrays.asList(Objects.requireNonNull(response.getBody()));
        model.addAttribute("urls", urlsList);

        return "view-urls";
    }
}

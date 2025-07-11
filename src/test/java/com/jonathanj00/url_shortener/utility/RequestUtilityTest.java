package com.jonathanj00.url_shortener.utility;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestUtilityTest {

    @Test
    public void testGetBaseUrl() {
        HttpServletRequest request = new MockHttpServletRequest("POST", "http://localhost:8080/shorten");
        String baseUrl = RequestUtility.getBaseUrl(request);

        assertEquals("http://localhost/", baseUrl);
    }
}

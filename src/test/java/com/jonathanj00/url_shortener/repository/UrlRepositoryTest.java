package com.jonathanj00.url_shortener.repository;

import com.jonathanj00.url_shortener.entity.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final String testUrl = "www.example.com";
    private final String testAlias = "testAlias";
    private final Url urlEntity = new Url(testUrl, testAlias);

    @BeforeEach
    public void setUp() {
        urlRepository.save(new Url(testUrl, testAlias));
    }

    @Test
    public void whenCalledSave_doNotSaveDuplicateUrl() {
        urlRepository.save(new Url(testUrl, "alias2"));

        assertThatThrownBy(() -> urlRepository.flush()).isInstanceOf(DataIntegrityViolationException.class);

        entityManager.clear();

        List<Url> urls = urlRepository.findAll();

        assertEquals(1, urls.size(), "Incorrect number of urls");
        assertEquals(urlEntity, urls.getFirst(), "Incorrect url saved in DB");
    }

    @Test
    public void whenCalledSave_doNotSaveDuplicateAlias() {
        urlRepository.save(new Url("www.example.com/page2", testAlias));

        assertThatThrownBy(() -> urlRepository.flush()).isInstanceOf(DataIntegrityViolationException.class);

        entityManager.clear();

        List<Url> urls = urlRepository.findAll();

        assertEquals(1, urls.size(), "Incorrect number of urls");
        assertEquals(urlEntity, urls.getFirst(), "Incorrect url saved in DB");
    }

    @Test
    public void whenCalledFindByUrl_returnsUrl() {
        List<Url> urls = urlRepository.findByUrl(testUrl);

        assertEquals(urlEntity, urls.getFirst(), "Incorrect url found in DB");
    }

    @Test
    public void whenCalledExistsByUrl_returnsTrue() {
        assertTrue(urlRepository.existsByUrl(testUrl), "Url should exist");
    }

    @Test
    public void whenCalledExistsByUrl_urlDoesNotExist_returnsFalse() {
        assertFalse(urlRepository.existsByUrl("incorrect url"), "Url should not exist");
    }

    @Test
    public void whenCalledExistsByAlias_returnsTrue() {
        assertTrue(urlRepository.existsByAlias(testAlias), "Url should exist");
    }

    @Test
    public void whenCalledExistsByAlias_aliasDoesNotExist_returnsFalse() {
        assertFalse(urlRepository.existsByAlias("incorrect alias"), "Url should not exist");
    }
}

package com.jonathanj00.url_shortener.repository;

import com.jonathanj00.url_shortener.entity.Url;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenCalledSave_thenCorrectNumberOfUsers() {
        urlRepository.save(new Url("bob@domain.com", "bob"));
        List<Url> urls = urlRepository.findAll();

        assertEquals(1, urls.size(), "Incorrect number of users");
    }

    @Test
    public void whenCalledSave_doNotSaveDuplicateEmail() {
        Url url = new Url("bob@domain.com", "bob");
        urlRepository.save(url);
        urlRepository.save(new Url("bob@domain.com", "bob2"));

        assertThatThrownBy(() -> urlRepository.flush()).isInstanceOf(DataIntegrityViolationException.class);

        entityManager.clear();

        List<Url> urls = urlRepository.findAll();

        assertEquals(1, urls.size(), "Incorrect number of users");
        assertEquals(url, urls.getFirst(), "Incorrect user saved in DB");
    }

    @Test
    public void whenCalledSave_doNotSaveDuplicateAlias() {
        Url url = new Url("bob@domain.com", "bob");
        urlRepository.save(url);
        urlRepository.save(new Url("bob2@domain.com", "bob"));

        assertThatThrownBy(() -> urlRepository.flush()).isInstanceOf(DataIntegrityViolationException.class);

        entityManager.clear();

        List<Url> urls = urlRepository.findAll();

        assertEquals(1, urls.size(), "Incorrect number of users");
        assertEquals(url, urls.getFirst(), "Incorrect user saved in DB");
    }
}

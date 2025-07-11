package com.jonathanj00.url_shortener.repository;

import com.jonathanj00.url_shortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Simple repository definition to handle URL entities
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    boolean existsByAlias(String alias);

    boolean existsByUrl(String url);

    List<Url> findByUrl(String url);

}

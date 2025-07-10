package com.jonathanj00.url_shortener.repository;

import com.jonathanj00.url_shortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Simple repository definition to handle URL entities
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
}

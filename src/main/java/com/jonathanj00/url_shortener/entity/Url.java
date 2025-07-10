package com.jonathanj00.url_shortener.entity;


import jakarta.persistence.*;

import java.util.Objects;

/**
 * Entity class defining mapping of URL's to alias's.
 * All values are required to be unique to avoid a URL being mapped to multiple alias's, or vice versa.
 */
@Entity
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true, nullable = false)
    private String url;
    @Column(unique = true, nullable = false)
    private String alias;

    public Url() {
    }

    public Url(String url, String alias) {
        this.url = url;
        this.alias = alias;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Url url1 = (Url) o;
        return Objects.equals(url, url1.url) && Objects.equals(alias, url1.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, alias);
    }
}

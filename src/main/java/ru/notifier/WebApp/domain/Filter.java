package ru.notifier.WebApp.domain;

import javax.persistence.*;

@Entity
@Table(name = "filter")
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String key;
    private String value;

    public Filter() {
    }

    public Filter(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

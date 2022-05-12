package ru.notifier.WebApp.domain;

// Сущность "рассылка" имеет атрибуты:
//
// уникальный id рассылки
// дата и время запуска рассылки
// текст сообщения для доставки клиенту
// фильтр свойств клиентов, на которых должна быть произведена рассылка (код мобильного оператора, тег)
// дата и время окончания рассылки: если по каким-то причинам не успели разослать все сообщения - никакие сообщения клиентам после этого времени доставляться не должны

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM yyyy HH:mm:ss")
    private LocalDateTime start_notification;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM yyyy HH:mm:ss")
    private LocalDateTime end_notification;
    private String message;


    @ElementCollection(targetClass = Filter.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "notification_filters", joinColumns = @JoinColumn(name = "notification_id"))
    private Set<Filter> filters;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Message> messages = new HashSet<>();


    public Notification() {
    }

    public Notification(Long id, LocalDateTime start_notification, LocalDateTime end_notification, String message, Set<Filter> filters, Set<Message> messages) {
        this.id = id;
        this.start_notification = start_notification;
        this.end_notification = end_notification;
        this.message = message;
        this.filters = filters;
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart_notification() {
        return start_notification;
    }

    public void setStart_notification(LocalDateTime start_notification) {
        this.start_notification = start_notification;
    }

    public LocalDateTime getEnd_notification() {
        return end_notification;
    }

    public void setEnd_notification(LocalDateTime end_notification) {
        this.end_notification = end_notification;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Filter> getFilters() {
        return filters;
    }

    public void setFilters(Set<Filter> filters) {
        this.filters = filters;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", start_notification=" + start_notification +
                ", end_notification=" + end_notification +
                ", message='" + message + '\'' +
                ", filters=" + filters +
                //", messages=" + messages +
                '}';
    }
}

// Сущность "сообщение" имеет атрибуты:
//
// уникальный id сообщения
// дата и время создания (отправки)
// статус отправки
// id рассылки, в рамках которой было отправлено сообщение
// id клиента, которому отправили

package ru.notifier.WebApp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime date_time_of_creation;
    private String message_text;
    private String status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn (name="notification_id")
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    public Message() {
    }

    public Message(LocalDateTime date_time_of_creation, String message_text, String status, Notification notification, Client client) {
        this.date_time_of_creation = date_time_of_creation;
        this.message_text = message_text;
        this.status = status;
        this.notification = notification;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeOfCreation() {
        return date_time_of_creation;
    }

    public void setDateTimeOfCreation(LocalDateTime date_time_of_creation) {
        this.date_time_of_creation = date_time_of_creation;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", date_time_of_creation=" + date_time_of_creation +
                ", message_text='" + message_text + '\'' +
                ", status='" + status + '\'' +
                ", notification=" + notification +
                ", client=" + client +
                '}';
    }
}
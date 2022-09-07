//  Сущность "клиент" имеет атрибуты:
//  - уникальный id клиента
//  - номер телефона клиента в формате 7XXXXXXXXXX (X - цифра от 0 до 9)
//  - код мобильного оператора
//  - тег (произвольная метка)
//  - часовой пояс

package ru.notifier.WebApp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Номер телефона не должен быть пустым.")
    @Column(name="phone", nullable=false)
    private Long phone;

    //@NotNull(message = "Код оператора не должен быть пустым.")
    //@Column(name="code_mobile_operator", nullable=false)
    private int code_mobile_operator;
    private String tag;
    private String time_zone;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Message> messages = new HashSet<>();

    public Client() {
    }

    public Client(Long phone, int code_mobile_operator, String tag, String time_zone, Set<Message> messages) {
        this.phone = phone;
        this.code_mobile_operator = code_mobile_operator;
        this.tag = tag;
        this.time_zone = time_zone;
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public int getCode_mobile_operator() {
        return code_mobile_operator;
    }

    public void setCode_mobile_operator(int code_mobile_operator) {
        this.code_mobile_operator = code_mobile_operator;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", phone=" + phone +
                ", code_mobile_operator=" + code_mobile_operator +
                ", tag='" + tag + '\'' +
                ", time_zone='" + time_zone + '\'' +
                //", messages=" + messages +
                '}';
    }
}

package ru.notifier.WebApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.notifier.WebApp.AbstractTest;
import ru.notifier.WebApp.domain.Notification;
import ru.notifier.WebApp.repositorys.NotificationRepository;
import ru.notifier.WebApp.sevices.NotificationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
class NotificationRestControllerTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;


    @BeforeEach
    @AfterEach
    public void resetDb() {
        notificationRepository.deleteAll();
    }

    @Test
    void testList() throws Exception {

        createTestNotification(
                new Notification(
                        LocalDateTime.parse("2022-05-03 13:00:00", formatter),
                        LocalDateTime.parse("2022-05-03 13:55:00", formatter),
                        "Летняя распродажа, поторопись!",
                        new HashSet<>(),
                        new HashSet<>()
                )
        );

        createTestNotification(
                new Notification(
                        LocalDateTime.parse("2022-05-03 13:20:00", formatter),
                        LocalDateTime.parse("2022-05-03 13:59:00", formatter),
                        "Зимняя распродажа, поторопись! Для оленей и снеговиков скидки.",
                        new HashSet<>(),
                        new HashSet<>()
                )
        );

        mockMvc.perform(get("/rest/api/notification").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].message", Matchers.equalTo("Летняя распродажа, поторопись!")))
                .andExpect(jsonPath("$[1].id", Matchers.notNullValue()))
                .andExpect(jsonPath("$[1].message", Matchers.equalTo("Зимняя распродажа, поторопись! Для оленей и снеговиков скидки.")));
    }

    @Test
    void testGetNotification() throws Exception {
        Notification n = createTestNotification(
                new Notification(
                        LocalDateTime.parse("2022-05-03 13:00:00", formatter),
                        LocalDateTime.parse("2022-05-03 13:55:00", formatter),
                        "Летняя распродажа, поторопись!",
                        new HashSet<>(),
                        new HashSet<>()
                )
        );

        long id = n.getId();
        mockMvc.perform(
                        get("/rest/api/notification/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.start_notification", Matchers.equalTo("03 мая 2022 13:00:00")))
                .andExpect(jsonPath("$.end_notification", Matchers.equalTo("03 мая 2022 13:55:00")))
                .andExpect(jsonPath("$.message", Matchers.equalTo("Летняя распродажа, поторопись!")));
    }

    @Test
    void testCreate() throws Exception {
        Notification n = createTestNotification(
                new Notification(
                        LocalDateTime.parse("2022-05-03 13:00:00", formatter),
                        LocalDateTime.parse("2022-05-03 13:55:00", formatter),
                        "Летняя распродажа, поторопись!",
                        new HashSet<>(),
                        new HashSet<>()
                )
        );
        long id = n.getId();

        String json = mapper.writeValueAsString(n);

        mockMvc.perform(post("/rest/api/notification").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.start_notification", Matchers.equalTo("03 мая 2022 13:00:00")))
                .andExpect(jsonPath("$.end_notification", Matchers.equalTo("03 мая 2022 13:55:00")))
                .andExpect(jsonPath("$.message", Matchers.equalTo("Летняя распродажа, поторопись!")));
    }

    @Test
    void testUpdate() throws Exception {
        Notification n = createTestNotification(
                new Notification(
                        LocalDateTime.parse("2022-05-03 13:00:00", formatter),
                        LocalDateTime.parse("2022-05-03 13:55:00", formatter),
                        "Летняя распродажа, поторопись!",
                        new HashSet<>(),
                        new HashSet<>()
                )
        );
        notificationService.getTimers().put(n.getId(), notificationService.createTimer(n));

        mockMvc.perform(
                        put("/rest/api/notification/{id}", n.getId())
                                .content(mapper.writeValueAsString(
                                        new Notification(
                                                LocalDateTime.parse("2022-05-03 13:05:00", formatter),
                                                LocalDateTime.parse("2022-05-03 14:05:00", formatter),
                                                "Летняя распродажа, поторопись!!!",
                                                new HashSet<>(),
                                                new HashSet<>()
                                        )
                                ))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.start_notification", Matchers.equalTo("03 мая 2022 13:05:00")))
                .andExpect(jsonPath("$.end_notification", Matchers.equalTo("03 мая 2022 14:05:00")))
                .andExpect(jsonPath("$.message", Matchers.equalTo("Летняя распродажа, поторопись!!!")));
    }

    @Test
    void testDelete() throws Exception {
        Notification n = createTestNotification(
                new Notification(
                        LocalDateTime.parse("2022-05-03 13:00:00", formatter),
                        LocalDateTime.parse("2022-05-03 13:55:00", formatter),
                        "Летняя распродажа, поторопись!",
                        new HashSet<>(),
                        new HashSet<>()
                )
        );
        notificationService.getTimers().put(n.getId(), notificationService.createTimer(n));
        mockMvc.perform(delete("/rest/api/notification/{id}", n.getId()).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Notification createTestNotification(Notification n) {
        return notificationRepository.save(n);
    }
}
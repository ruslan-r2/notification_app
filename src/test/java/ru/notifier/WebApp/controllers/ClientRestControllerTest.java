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
import ru.notifier.WebApp.domain.Client;
import ru.notifier.WebApp.repositorys.ClientRepository;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
class ClientRestControllerTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    @AfterEach
    public void resetDb() {
        clientRepository.deleteAll();
    }

    @Test
    void testList() throws Exception {
        createTestClient(new Client(79181112233L, 918, "VIP", "UTC+03:00", new HashSet<>()));
        createTestClient(new Client(79281112233L, 928, "GOLD", "UTC+04:00", new HashSet<>()));
        createTestClient(new Client(79881112233L, 988, "SILVER", "UTC+05:00", new HashSet<>()));

        mockMvc.perform(get("/rest/api/client").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].phone", Matchers.equalTo(79181112233L)))
                .andExpect(jsonPath("$[1].code_mobile_operator", Matchers.equalTo(928)))
                .andExpect(jsonPath("$[1].tag", Matchers.equalTo("GOLD")))
                .andExpect(jsonPath("$[2].time_zone", Matchers.equalTo("UTC+05:00")));

    }

    @Test
    void testGetClient() throws Exception {
        Client client = createTestClient(new Client(79181112233L, 918, "VIP", "UTC+03:00", new HashSet<>()));
        long id = client.getId();
        mockMvc.perform(
                        get("/rest/api/client/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value(79181112233L))
                .andExpect(jsonPath("$.code_mobile_operator").value(918));
    }

    @Test
    void testCreate() throws Exception {
        Client client = new Client(79280445678L, 928, "VIP", "UTC+02:00", new HashSet<>());
        String json = mapper.writeValueAsString(client);

        mockMvc.perform(post("/rest/api/client").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.phone", Matchers.equalTo(79280445678L)))
                .andExpect(jsonPath("$.code_mobile_operator", Matchers.equalTo(928)))
                .andExpect(jsonPath("$.tag", Matchers.equalTo("VIP")))
                .andExpect(jsonPath("$.time_zone", Matchers.equalTo("UTC+02:00")));

    }

    @Test
    void testUpdate() throws Exception {
        Client client = createTestClient(new Client(79181112233L, 918, "VIP", "UTC+03:00", new HashSet<>()));
        long id = client.getId();
        mockMvc.perform(
                        put("/rest/api/client/{id}", id)
                                .content(mapper.writeValueAsString(new Client(79281112233L, 928, "VIP", "UTC+03:00", new HashSet<>())))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value(79281112233L))
                .andExpect(jsonPath("$.code_mobile_operator").value(928));
    }

    @Test
    void testDelete() throws Exception {
        Client client = createTestClient(new Client(79181112233L, 918, "VIP", "UTC+03:00", new HashSet<>()));
        mockMvc.perform(delete("/rest/api/client/{id}", client.getId()).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Client createTestClient(Client client) {
        return clientRepository.save(client);
    }

}
package ru.notifier.WebApp.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.notifier.WebApp.domain.Client;
import ru.notifier.WebApp.repositorys.ClientRepository;
import ru.notifier.WebApp.repositorys.MessageRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/api/client")
public class ClientRestController {

    private final ClientRepository clientRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ClientRestController(ClientRepository clientRepository, MessageRepository messageRepository) {
        this.clientRepository = clientRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public List<Client> list() {
        return clientRepository.findAll();
    }

    @GetMapping("{id}")
    public Client getClient(@PathVariable("id") Client client) {
        return client;
    }

    @PostMapping
    public Client create(@Valid @RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("{id}")
    public Client update(@PathVariable("id") Client clientFromDb, @Valid @RequestBody Client client) {
        BeanUtils.copyProperties(client, clientFromDb, "id");
        return clientRepository.save(clientFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Client client) {
        // сначала удаляем все сообщения пользователя
        messageRepository.deleteAllInBatch(client.getMessages());
        // потом удаляем пользователя
        clientRepository.delete(client);
    }

}
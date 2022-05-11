package ru.notifier.WebApp.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.notifier.WebApp.domain.Message;
import ru.notifier.WebApp.repositorys.MessageRepository;

import java.util.List;

@RestController
@RequestMapping("/rest/api/message")
public class MessageRestController {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageRestController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public List<Message> list() {
        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    public Message getMessage(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        return messageRepository.save(message);
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb, @RequestBody Message message) {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        return messageRepository.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepository.delete(message);
    }

}

package ru.notifier.WebApp.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import ru.notifier.WebApp.domain.Notification;
import ru.notifier.WebApp.repositorys.NotificationRepository;

import java.util.List;



@RestController
@RequestMapping("/rest/api/notification")
public class NotificationRestController {

    private final NotificationRepository notificationRepository;

    public NotificationRestController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public List<Notification> list() {
        return notificationRepository.findAll();
    }

    @GetMapping("{id}")
    public Notification getNotification(@PathVariable("id") Notification notification) {
        return notification;
    }

    @PostMapping
    public Notification create(@RequestBody Notification notification) {
        return notificationRepository.save(notification);
    }

    @PutMapping("{id}")
    public Notification update(@PathVariable("id") Notification notificationFromDb, @RequestBody Notification notification) {
        BeanUtils.copyProperties(notification, notificationFromDb, "id");
        return notificationRepository.save(notificationFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Notification notification) {
        notificationRepository.delete(notification);
    }

}

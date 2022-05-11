package ru.notifier.WebApp.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import ru.notifier.WebApp.domain.Notification;
import ru.notifier.WebApp.repositorys.NotificationRepository;
import ru.notifier.WebApp.sevices.NotificationService;

import java.util.List;


@RestController
@RequestMapping("/rest/api/notification")
public class NotificationRestController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public NotificationRestController(NotificationRepository notificationRepository, NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
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
        Notification n = notificationRepository.save(notification);
        notificationService.getTimers().put(n.getId(), notificationService.createTimer(n));
        return n;
    }

    @PutMapping("{id}")
    public Notification update(@PathVariable("id") Notification notificationFromDb, @RequestBody Notification notification) {
        BeanUtils.copyProperties(notification, notificationFromDb, "id");
        Notification n = notificationRepository.save(notificationFromDb);
        notificationService.getTimers().get(n.getId()).cancel();
        notificationService.getTimers().put(n.getId(), notificationService.createTimer(n));
        return n;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Notification notification) {
        notificationRepository.delete(notification);
        notificationService.getTimers().get(notification.getId()).cancel();
    }

}

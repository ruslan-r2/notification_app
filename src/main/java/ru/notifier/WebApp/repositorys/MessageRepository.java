package ru.notifier.WebApp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.notifier.WebApp.domain.Client;
import ru.notifier.WebApp.domain.Filter;
import ru.notifier.WebApp.domain.Message;
import ru.notifier.WebApp.domain.Notification;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {

    Message findByClientAndNotification(Client c, Notification n);

}

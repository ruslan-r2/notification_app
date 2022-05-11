package ru.notifier.WebApp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.notifier.WebApp.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

}

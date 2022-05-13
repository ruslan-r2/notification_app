package ru.notifier.WebApp.sevices;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.notifier.WebApp.aop.LoggingAspect;
import ru.notifier.WebApp.domain.Client;
import ru.notifier.WebApp.domain.Filter;
import ru.notifier.WebApp.domain.Message;
import ru.notifier.WebApp.domain.Notification;
import ru.notifier.WebApp.repositorys.ClientRepository;
import ru.notifier.WebApp.repositorys.MessageRepository;
import ru.notifier.WebApp.repositorys.NotificationRepository;
import ru.notifier.WebApp.repositorys.spec.ClientSpecification;
import ru.notifier.WebApp.repositorys.spec.SearchCriteria;
import ru.notifier.WebApp.repositorys.spec.SearchOperation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;

@Service
public class NotificationService extends Timer {

    private final NotificationRepository notificationRepository;
    private final ClientRepository clientRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    private Map<Long,Timer> timers = new HashMap<>();
    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    public NotificationService(NotificationRepository notificationRepository,
                               ClientRepository clientRepository,
                               MessageRepository messageRepository,
                               MessageService messageService) {

        this.notificationRepository = notificationRepository;
        this.clientRepository = clientRepository;
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }

    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    public Map<Long, Timer> getTimers() {
        return timers;
    }

    public Timer createTimer(Notification notification) {

        Timer timer = new Timer();

        LocalDateTime startDateTime = notification.getStart_notification();
        LocalDateTime endDateTime = notification.getEnd_notification();

        Long start_notification = startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long end_notification = endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        if (start_notification < now && now < end_notification) {
            logger.info("Активная рассылка: " + notification);
            logger.info("Таймер: " + timer.toString());
            // start task
            timer.schedule(createTimerTask(notification.getFilters(), notification, timer.toString()), 5 * 1000);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.info("Останавливаем таймер: " + timer.toString());
                    timer.cancel();
                }
            }, end_notification - now);

        } else if (start_notification > now && now < end_notification) {
            logger.info("Отложенная рассылка: " + notification);
            logger.info("Таймер: " + timer.toString());
            // start task
            timer.schedule(createTimerTask(notification.getFilters(), notification, timer.toString()), start_notification - now); //
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.info("Останавливаем таймер: " + timer.toString());
                    timer.cancel();
                }
            }, end_notification - now);

        }

        return timer;
    }

    public TimerTask createTimerTask(Set<Filter> filters, Notification notification, String timerName) {
        return new TimerTask() {
            @Override
            public void run() {

                logger.info("Начинаем рассылать сообщения! " + timerName);
                ClientSpecification searchClient = new ClientSpecification();
                for (Filter filter : filters) {
                    searchClient.add(new SearchCriteria(filter.getKey(), filter.getValue(), SearchOperation.EQUAL));
                }

                List<Client> clients = clientRepository.findAll(searchClient);
                //clients.forEach(System.out::println);
                for (Client client : clients) {
                    // Проверяем есть ли сообщение у клиента по данной рассылке.
                    Message message = messageRepository.findByClientAndNotification(client, notification);
                    if (message == null) {
                        // create new message datatime text status nitification, client
                        message = new Message(LocalDateTime.now(), notification.getMessage(), "NEW", notification, client);
                        // Сохраняем сообщение в базу. получаем id сообщения
                        message = messageRepository.save(message);

                        logger.info("Отправляем новое сообщение клиенту c id " + client.getId() + " | " + message);

                        HttpStatus httpStatus = messageService.sendMessage(message);
                        // проверяем статус отправленного сообщения
                        if ( httpStatus == HttpStatus.OK) {
                            message.setStatus("DELIVERED");
                            messageRepository.save(message);
                        }else{
                            message.setStatus("NOT_DELIVERED");
                            messageRepository.save(message);
                            logger.warning("Сообщение не было доставлено!!! " + message );
                        }

                    } else {
                        if (message.getStatus().equals("NOT_DELIVERED")) {
                            // отправляем сообщение еще раз
                            logger.info("Отправляем повторно сообщение клиенту c id " + client.getId() + " | " + message);
                            HttpStatus httpStatus = messageService.sendMessage(message);
                            // проверяем статус отправленного сообщения
                            if ( httpStatus == HttpStatus.OK) {
                                message.setStatus("DELIVERED");
                                messageRepository.save(message);
                            }else {
                                logger.warning("Сообщение не было доставлено!!! " + message );
                            }
                        }
                    }
                    logger.info( "Клиент id = " + client.getId() + " | " + message);
                }
            }
        };
    }

    @Bean
    public void run() {
        List<Notification> notifications = getNotifications();
        int count = notifications.size();
        logger.info("Всего в базе рассылок: " + count);

        for (Notification n : notifications) {
            timers.put(n.getId(), createTimer(n));
        }

    }

}

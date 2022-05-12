package ru.notifier.WebApp.sevices;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

@Service
public class NotificationService extends Timer {

    private final NotificationRepository notificationRepository;
    private final ClientRepository clientRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    private Map<Long,Timer> timers = new HashMap<>();

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
            System.out.println("Активная рассылка: " + notification);
            System.out.println("таймер" + timer.toString());
            // start task
            timer.schedule(createTimerTask(notification.getFilters(), notification, timer.toString()), 5 * 1000);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Останавливаем таймер " + timer.toString());
                    timer.cancel();
                }
            }, end_notification - now);

        } else if (start_notification > now && now < end_notification) {
            System.out.println("Отложенная рассылка: " + notification);
            System.out.println("таймер" + timer.toString());
            // start task
            timer.schedule(createTimerTask(notification.getFilters(), notification, timer.toString()), start_notification - now); //
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Останавливаем таймер " + timer.toString());
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

                System.out.println("Начинаем рассылать сообщения! " + timerName);
                ClientSpecification searchClient = new ClientSpecification();
                for (Filter filter : filters) {
                    System.out.println("Filter: " + filter);
                    searchClient.add(new SearchCriteria(filter.getKey(), filter.getValue(), SearchOperation.EQUAL));
                }

                List<Client> clients = clientRepository.findAll(searchClient);
                clients.forEach(System.out::println);
                for (Client client : clients) {
                    Message message = messageRepository.findByClientAndNotification(client, notification);
                    System.out.println("Message: " + message);
                    if (message == null) {
                        // create new message datatime text status nitification, client
                        message = new Message(LocalDateTime.now(), notification.getMessage(), "NEW", notification, client);
                        // Сохраняем сообщение в базу. получаем id сообщения
                        message = messageRepository.save(message);

                        System.out.println("Отправляем новое сообщение клиенту " + client.toString());
                        System.out.println(message.toString());

                        HttpStatus httpStatus = messageService.sendMessage(message);
                        // проверяем статус отправленного сообщения
                        if ( httpStatus == HttpStatus.OK) {
                            message.setStatus("DELIVERED");
                            messageRepository.save(message);
                        }else{
                            message.setStatus("NOT_DELIVERED");
                            messageRepository.save(message);
                            System.out.println("Сообщение не было доставлено!!!");
                        }

                    } else {
                        if (message.getStatus().equals("NOT_DELIVERED")) {
                            // отправляем сообщение еще раз
                            System.out.println("Отправляем повторно сообщение клиенту " + client.toString());
                            System.out.println(message.toString());
                            HttpStatus httpStatus = messageService.sendMessage(message);
                            // проверяем статус отправленного сообщения
                            if ( httpStatus == HttpStatus.OK) {
                                message.setStatus("DELIVERED");
                                messageRepository.save(message);
                            }else {
                                System.out.println("Сообщение не было доставлено!!!");
                            }
                        }
                    }
                }
            }
        };
    }

    @Bean
    public void run() {
        List<Notification> notifications = getNotifications();
        int count = notifications.size();
        System.out.println("Рассылок в базе: " + count);

        for (Notification n : notifications) {
            timers.put(n.getId(), createTimer(n));
        }

    }

}

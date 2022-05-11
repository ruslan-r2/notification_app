insert into client(id, phone, code_mobile_operator, tag, time_zone)
    values
        (1, 79181112233, 918, 'VIP', 'UTC+03:00'),
        (2, 79282223344, 928, 'GOLD', 'UTC+03:30'),
        (3, 79883334455, 988, 'SILVER', 'UTC+04:00'),
        (4, 79184445566, 918, 'BRONZ', 'UTC+05:30'),
        (5, 79185556677, 918, 'VIP', 'UTC+06:30'),
        (6, 79886667788, 988, 'BRONZ', 'UTC+07:30'),
        (7, 79182345678, 918, 'SILVER', 'UTC+08:30'),
        (8, 79184567890, 918, 'BRONZ', 'UTC+09:30'),
        (9, 79881234567, 988, 'BRONZ', 'UTC+04:00'),
        (10, 79283456789, 928, 'GOLD', 'UTC+04:00');

insert into filter(id, key, value)
    values
        (1, 'code_mobile_operator', '918'),
        (2, 'code_mobile_operator', '928'),
        (3, 'code_mobile_operator', '988'),
        (4, 'tag', 'VIP'),
        (5, 'tag', 'GOLD'),
        (6, 'tag', 'SILVER'),
        (7, 'tag', 'BRONZ');

insert into notification(id, start_notification, end_notification, message)
    values
        (1, '2022-05-03T13:00:00', '2022-05-03T13:55:00', 'Летняя распродажа, поторопись!'),
        (2, '2022-05-03T13:20:00', '2022-05-03T13:59:00', 'Зимняя распродажа, поторопись! Для оленей скидки.');

insert into notification_filters( notification_id, filters_id)
    values
        (1, 1),
        (1, 4),
        (2, 3),
        (2, 7);


insert into message(id, message_text, date_time_of_creation, status, client_id, notification_id)
    values
        (1, 'Летняя распродажа, поторопись!', '2022-04-18T14:05:23', 'DELIVERED', 1, 1),
        (2, 'Летняя распродажа, поторопись!', '2022-04-18T14:06:56', 'DELIVERED', 9, 1),
        (3, 'Зимняя распродажа, поторопись! Для оленей скидки.', '2022-04-18T14:05:23', 'DELIVERED', 6, 2),
        (4, 'Зимняя распродажа, поторопись! Для оленей скидки.', '2022-04-18T14:05:23', 'DELIVERED', 7, 2),
        (5, 'Зимняя распродажа, поторопись! Для оленей скидки.', '2022-04-18T14:05:23', 'DELIVERED', 8, 2);
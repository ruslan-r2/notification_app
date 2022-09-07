create sequence hibernate_sequence start 100 increment 1;

    create table client (
       id int8 not null,
        code_mobile_operator int4 not null,
        phone int8 not null,
        tag varchar(255),
        time_zone varchar(255),
        primary key (id)
    );

    create table filter (
       id int8 not null,
        key varchar(255),
        value varchar(255),
        primary key (id)
    );

    create table message (
       id int8 not null,
        date_time_of_creation timestamp,
        message_text varchar(255),
        status varchar(255),
        client_id int8 not null,
        notification_id int8 not null,
        primary key (id, client_id, notification_id)
    );

    create table notification (
       id int8 not null,
        end_notification timestamp,
        message varchar(255),
        start_notification timestamp,
        primary key (id)
    );

    create table notification_filters (
       notification_id int8 not null,
        filters_id int8 not null,
        primary key (notification_id, filters_id)
    );

    create table roles (
       id int8 not null,
        name varchar(255),
        primary key (id)
    );

    create table user_role (
       user_id int8 not null,
        roles_id int8 not null,
        primary key (user_id, roles_id)
    );

    create table usr (
       id int8 not null,
        active boolean not null,
        email varchar(255),
        name varchar(255),
        password varchar(255),
        surname varchar(255),
        username varchar(255),
        primary key (id)
    );

--    alter table notification_filters
--       add constraint UK_s6nqeetctduao5dby4yi0xkqq unique (filters_id);


--     alter table user_role
--        add constraint UK_5k3dviices5fr7560hvc81x4r unique (roles_id);


    alter table message
       add constraint FKgvo2vw6xgtws6ognjlnrxyi5x
       foreign key (client_id)
       references client;


    alter table message
       add constraint FKj48auadp2gby47w3v6oc0jbyv
       foreign key (notification_id)
       references notification;


    alter table notification_filters
       add constraint FKlhwjs0h3kqfo0861q8woy1nyq
       foreign key (filters_id)
       references filter;


    alter table notification_filters
       add constraint FKp5mn2btcigro62ok9nhrip5do
       foreign key (notification_id)
       references notification;


    alter table user_role
       add constraint FK66ou45fyydgltrhvuc81rp15q
       foreign key (roles_id)
       references roles;


    alter table user_role
       add constraint FKfpm8swft53ulq2hl11yplpr5
       foreign key (user_id)
       references usr;
insert into usr(id, username, password, email, surname, name, active)
    values (1, 'admin', '123', 'admin@admin.ru', 'Repin', 'Ruslan', true);

insert into roles(id, name)
    values (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

insert into user_role(user_id, roles_id)
    values (1, 1), (1, 2);
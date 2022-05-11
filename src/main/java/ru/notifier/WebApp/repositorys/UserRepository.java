package ru.notifier.WebApp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.notifier.WebApp.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
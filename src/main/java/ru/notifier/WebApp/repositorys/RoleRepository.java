package ru.notifier.WebApp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.notifier.WebApp.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

package ru.notifier.WebApp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.notifier.WebApp.domain.Filter;
import java.util.List;


public interface FilterRepository extends JpaRepository<Filter,Long> {

    List<Filter> findAllByKey(String str);

}

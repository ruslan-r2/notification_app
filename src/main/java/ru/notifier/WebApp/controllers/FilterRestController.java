package ru.notifier.WebApp.controllers;

import org.springframework.web.bind.annotation.*;
import ru.notifier.WebApp.domain.Filter;
import ru.notifier.WebApp.repositorys.FilterRepository;

import java.util.List;




@RestController
@RequestMapping("/rest/api/filter")
public class FilterRestController {

    private final FilterRepository filterRepository;

    public FilterRestController(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }


    @GetMapping("{key}")
    public List<Filter> listByKey(@PathVariable("key") String key) {
        return filterRepository.findAllByKey(key);
    }


}

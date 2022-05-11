package ru.notifier.WebApp.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.notifier.WebApp.domain.User;
import ru.notifier.WebApp.repositorys.UserRepository;
import java.util.*;

@RestController
@RequestMapping("/rest/api/user")
public class UserRestController {

    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> list() {
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    public User getClient(@PathVariable("id") User user) {
        return user;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("{id}")
    public User update(@PathVariable("id") User userFromDb, @RequestBody User user) {
        BeanUtils.copyProperties(user, userFromDb, "id");
        return userRepository.save(userFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") User user) {
        userRepository.delete(user);
    }

}

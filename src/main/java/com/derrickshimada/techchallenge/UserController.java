package com.derrickshimada.techchallenge;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/User")
    public List<User> all() {
        return repository.findAll();
    }

    @PostMapping("/AddUser")
    public User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    @GetMapping("/GetUserByUsername/{username}")
    public User findByUsername(@PathVariable String username) {

        return repository.findById(username)
        .orElseThrow(() -> new UserNotFoundException(username));
    }

    @PutMapping("/UpdateUser/{username}")
    public User updateUser(@RequestBody User newUser, @PathVariable String username) {

        return repository.findById(username)
        .map(user -> {
            user.setName(newUser.getName());
            user.setEmail(newUser.getEmail());
            return repository.save(user);
        })
        .orElseGet(() -> {
            newUser.setUsername(username);
            return repository.save(newUser);
        });
    }

    @DeleteMapping("/DeleteUser/{username}")
    public void deleteUser(@PathVariable String username) {
        repository.deleteById(username);
  }
}
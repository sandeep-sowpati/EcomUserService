package dev.sansow.ecomuserservice.controller;

import dev.sansow.ecomuserservice.model.Session;
import dev.sansow.ecomuserservice.model.User;
import dev.sansow.ecomuserservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> allUsers(){
        return ResponseEntity.ok(userService.allUsers());
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> allSessions(){
        return ResponseEntity.ok(userService.allSessions());
    }
}

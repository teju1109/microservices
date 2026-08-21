package com.example.userservice.controller;

import com.example.userservice.dto.ErrorResponse;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Value("${app.message}")
    private String message;

    @Value("${app.environment}")
    private String environment;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {

        User user = userService.getUserById(id);

        if (user == null) {
            ErrorResponse errorResponse =
                    new ErrorResponse("User not found with id: " + id);

            return ResponseEntity.status(404).body(errorResponse);
        }

        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/slow/{id}")
    public UserResponse slowUser(@PathVariable Long id)
            throws InterruptedException {

        Thread.sleep(10000);

        return new UserResponse(
                id,
                "John",
                "john@example.com"
        );
    }

    @GetMapping("/config")
    public String getConfig() {
        return "Message: " + message +
               " | Environment: " + environment;
    }
}
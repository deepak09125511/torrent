package com.torrent.torrentApplication.auth.Controller;

import com.torrent.torrentApplication.auth.Model.User;
import com.torrent.torrentApplication.auth.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        }
        catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try{
            User loggedInUser = userService.loginUser(
                    user.getEmail(),
                    user.getPasswordHash()
                    );
            return ResponseEntity.ok(loggedInUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}

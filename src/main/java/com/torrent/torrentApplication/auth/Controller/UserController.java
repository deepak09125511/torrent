package com.torrent.torrentApplication.auth.Controller;

import com.torrent.torrentApplication.auth.DTO.LoginResponseDTO;
import com.torrent.torrentApplication.auth.Model.User;
import com.torrent.torrentApplication.auth.Service.UserService;
import com.torrent.torrentApplication.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService,JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        System.out.println(user.getUserName());
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
            User loggedInUser = userService.loginUser(user.getEmail(), user.getPasswordHash());
            String token = jwtUtil.generateToken(loggedInUser.getEmail());
            LoginResponseDTO responseDTO = new LoginResponseDTO(
                    token,
                    loggedInUser.getUser_id(),
                    loggedInUser.getUserName()
            );
            return  ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

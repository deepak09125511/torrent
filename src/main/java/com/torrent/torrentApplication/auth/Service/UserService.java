package com.torrent.torrentApplication.auth.Service;

import com.torrent.torrentApplication.auth.Model.User;

import com.torrent.torrentApplication.auth.Repository.UserRepository;
import com.torrent.torrentApplication.security.CustomUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public UserService(UserRepository userRepository,CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
    }

    public User registerUser(User user) {
        User existingUser =
                userRepository.findByEmail(user.getEmail());

        if(existingUser != null) {
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(user);
    }
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        if(!user.getPasswordHash().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}

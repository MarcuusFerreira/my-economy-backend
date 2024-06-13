package com.mobile.login_auth_api.domain.service;

import com.mobile.login_auth_api.domain.user.User;
import com.mobile.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findUser(String email) throws RuntimeException {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
    }
}

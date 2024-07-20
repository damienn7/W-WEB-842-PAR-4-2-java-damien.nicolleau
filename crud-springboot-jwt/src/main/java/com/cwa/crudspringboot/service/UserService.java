package com.cwa.crudspringboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cwa.crudspringboot.entity.User;
import com.cwa.crudspringboot.repository.UserRepository;

public class UserService {
        @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

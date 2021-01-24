package com.example.recruitmentapp.service;

import com.example.recruitmentapp.entity.User;
import com.example.recruitmentapp.exception.UserNotFoundException;
import com.example.recruitmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getBy(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setLogin(updatedUser.getLogin());
                    user.setName(updatedUser.getName());
                    user.setPassword(updatedUser.getPassword());
                    return userRepository.save(updatedUser);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}

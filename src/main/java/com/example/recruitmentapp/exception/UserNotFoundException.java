package com.example.recruitmentapp.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Could not find user with id: " + id);
    }

    public UserNotFoundException(String name) {
        super("Could not find user with name: " + name);
    }

}

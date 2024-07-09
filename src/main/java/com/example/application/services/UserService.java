package com.example.application.services;

import com.example.application.entities.User;

public interface UserService {

  User saveUser(User user);

  User findByContactNumber(long contactNumber);
}

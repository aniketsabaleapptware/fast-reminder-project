package com.example.application.services.impl;

import com.example.application.entities.User;
import com.example.application.repository.UserRepository;
import com.example.application.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }


  @Override
  public User findByContactNumber(long contactNumber){
    return userRepository.findByContactNumber(contactNumber);
  }
}

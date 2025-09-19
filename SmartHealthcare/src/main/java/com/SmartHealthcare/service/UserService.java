package com.SmartHealthcare.service;

import com.SmartHealthcare.Impl.UserServiceImpl;
import com.SmartHealthcare.model.User;
import com.SmartHealthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserServiceImpl userServiceImpl;

    public User getUserByEmail (String email){
        return userRepo.findByuserEmail(email);
    }

    public void saveUser(User user){
        userRepo.save(user);
    }

    public List<User>getAllUsers(){
        return userServiceImpl.getAllUsers();

    }

}

package com.SmartHealthcare.Impl;


import com.SmartHealthcare.model.Patient;
import com.SmartHealthcare.model.User;
import com.SmartHealthcare.repository.PatientRepository;
import com.SmartHealthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}

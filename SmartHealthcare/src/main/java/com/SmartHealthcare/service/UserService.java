package com.SmartHealthcare.service;

import com.SmartHealthcare.Impl.UserServiceImpl;
import com.SmartHealthcare.model.User;
import com.SmartHealthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserServiceImpl userServiceImpl;

    public User getUserByEmail (String email){
        return userRepo.findByuserEmail(email);
    }

    @Transactional
    public User saveUser(User user){
        return userRepo.save(user);

    }

    public List<User>getAllUsers(){
        return userServiceImpl.getAllUsers();

    }
    public void deleteUser(Long id){userRepo.deleteById(id);}

}

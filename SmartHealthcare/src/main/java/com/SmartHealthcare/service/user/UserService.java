package com.SmartHealthcare.service.user;

import com.SmartHealthcare.model.user.User;
import java.util.List;

public interface UserService {

    User getUserByEmail(String email);

    User saveUser(User user);

    List<User> getAllUsers();

    void deleteUser(Long id);
}

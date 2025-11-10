package com.SmartHealthcare.Impl.user;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.exception.DuplicateResourceException;
import com.SmartHealthcare.exception.ResourceNotFoundException;
import com.SmartHealthcare.model.user.User;
import com.SmartHealthcare.repository.user.UserRepository;
import com.SmartHealthcare.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        try {
            log.info("Fetching user by email: {}", email);
            User user = userRepository.findByuserEmail(email);
            if (user == null) {
                log.warn("User not found with email: {}", email);
                throw new ResourceNotFoundException(ServiceCodes.NOT_FOUND);
            }
            return user;
        } catch (Exception ex) {
            log.error("Error fetching user by email: {}", email, ex);
            throw ex; // Handled by GlobalExceptionHandler
        }
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        try {
            User existingUser = userRepository.findByuserEmail(user.getUserEmail());

            if (existingUser != null) {
                log.warn("Duplicate user creation attempt: {}", user.getUserEmail());
                throw new DuplicateResourceException(ServiceCodes.RESOURCE_CONFLICT);
            }
            return userRepository.save(user);
        } catch (Exception ex) {
            log.error("Error saving user: {}", user.getUserEmail(), ex);
            throw ex;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            log.info("Fetching all users");
            return userRepository.findAll();
        } catch (Exception ex) {
            log.error("Error fetching all users", ex);
            throw ex;
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            log.info("Deleting user with ID: {}", id);
            if (!userRepository.existsById(id)) {
                log.warn("Attempted to delete non-existing user ID: {}", id);
                throw new ResourceNotFoundException(ServiceCodes.NOT_FOUND);
            }
            userRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error deleting user with ID: {}", id, ex);
            throw ex;
        }
    }
}

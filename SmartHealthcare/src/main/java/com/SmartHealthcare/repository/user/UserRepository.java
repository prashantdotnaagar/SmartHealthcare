package com.SmartHealthcare.repository.user;

import com.SmartHealthcare.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
     User findByuserEmail(String email);

     User findByUserName(String username);
}

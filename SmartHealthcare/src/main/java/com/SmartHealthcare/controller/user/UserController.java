package com.SmartHealthcare.controller.user;

import com.SmartHealthcare.model.user.User;
import com.SmartHealthcare.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    ResponseEntity<List<User>>getAllUsers(){
        List<User>users=userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Successfully");
    }

//    @GetMapping("/{id}")
//    ResponseEntity user
}

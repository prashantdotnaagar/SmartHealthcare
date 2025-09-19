package com.SmartHealthcare.controller;

import com.SmartHealthcare.model.Admin;
import com.SmartHealthcare.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all")
    ResponseEntity<List<Admin>>getAllAdmin(){
        List<Admin>admins=adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

}

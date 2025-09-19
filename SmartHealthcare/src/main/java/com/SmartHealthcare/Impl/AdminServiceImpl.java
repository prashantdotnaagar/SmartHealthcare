package com.SmartHealthcare.Impl;

import com.SmartHealthcare.model.Admin;
import com.SmartHealthcare.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl {

    @Autowired
    private AdminRepository adminRepository;
    public List<Admin> getAllAdmin(){
        return adminRepository.findAll();
    }

}

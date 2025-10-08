package com.SmartHealthcare.service;


import com.SmartHealthcare.Impl.AdminServiceImpl;
import com.SmartHealthcare.model.Admin;
import com.SmartHealthcare.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminServiceImpl adminServiceimpl;

    public void saveAdmin(Admin admin){
        adminRepository.save(admin);
    }

    public List<Admin>getAllAdmins(){
        return adminServiceimpl.getAllAdmin();
    }

    public Admin getAdminById(Long id) {return adminRepository.getReferenceById(id);}

}

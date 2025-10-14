package com.SmartHealthcare.Impl.admin;

import com.SmartHealthcare.model.admin.Admin;
import com.SmartHealthcare.repository.admin.AdminRepository;
import com.SmartHealthcare.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.getReferenceById(id);
    }
}

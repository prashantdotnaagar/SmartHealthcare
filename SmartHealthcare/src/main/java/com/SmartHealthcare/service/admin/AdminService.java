package com.SmartHealthcare.service.admin;

import com.SmartHealthcare.model.admin.Admin;
import java.util.List;

public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAllAdmins();

    Admin getAdminById(Long id);
}

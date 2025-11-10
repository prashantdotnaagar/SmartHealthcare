package com.SmartHealthcare.Impl.admin;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.exception.ResourceNotFoundException;
import com.SmartHealthcare.exception.ServiceException;
import com.SmartHealthcare.model.admin.Admin;
import com.SmartHealthcare.repository.admin.AdminRepository;
import com.SmartHealthcare.service.admin.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional
    public void saveAdmin(Admin admin) {
        try {
            log.info("Saving admin");
            adminRepository.save(admin);
            log.info("Successfully saved admin");
        } catch (DataAccessException e) {
            log.error("Database error while saving admin: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while saving admin: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.SAVE_FAILED);
        }
    }

    @Override
    public List<Admin> getAllAdmins() {
        try {
            log.info("Fetching all admins");
            return adminRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Database error while fetching all admins: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching all admins: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }

    @Override
    public Admin getAdminById(Long id) {
        try {
            log.info("Fetching admin with ID: {}", id);
            return adminRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Admin not found with ID: {}", id);
                        return new ResourceNotFoundException(ServiceCodes.ADMIN_NOT_FOUND);
                    });
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while fetching admin with ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching admin with ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }
}
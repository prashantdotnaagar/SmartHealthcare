package com.SmartHealthcare.controller.admin;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.dto.response.admin.AdminByIdResDTO;
import com.SmartHealthcare.model.admin.Admin;
import com.SmartHealthcare.service.admin.AdminService;
import com.SmartHealthcare.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    ResponseEntity<List<Admin>>getAllAdmin(){
        List<Admin>admins=adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteAdmin(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Successfully");
    }


    @GetMapping("/{id}")
    ResponseEntity getAdminById(@PathVariable Long id){
        try {
            Admin admin= adminService.getAdminById(id);
            AdminByIdResDTO dto = new AdminByIdResDTO();
            dto.setAccessLevel(admin.getAccessLevel());
            dto.setDepartment(admin.getDepartment());
            dto.setFirstName(admin.getFirstName());
            dto.setLastName(admin.getLastName());
            dto.setAdminId(admin.getAdminId());
            if(admin != null)return ResponseEntity.ok(dto);
            else return ResponseEntity.badRequest().body(ServiceCodes.ADMIN_NOT_FOUND);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }



}

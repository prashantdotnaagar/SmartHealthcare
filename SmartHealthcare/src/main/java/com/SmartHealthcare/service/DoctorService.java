package com.SmartHealthcare.service;


import com.SmartHealthcare.Impl.DoctorServiceImpl;
import com.SmartHealthcare.model.Doctor;
import com.SmartHealthcare.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private DoctorServiceImpl doctorServiceimpl;

    public void saveDoctor(Doctor doctor){
        doctorRepo.save(doctor);
    }

    public List<Doctor> getAllDoctor(){return  doctorServiceimpl.getAllDoctor();}

}

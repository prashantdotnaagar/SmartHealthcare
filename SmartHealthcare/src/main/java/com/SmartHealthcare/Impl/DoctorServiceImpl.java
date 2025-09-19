package com.SmartHealthcare.Impl;

import com.SmartHealthcare.model.Doctor;
import com.SmartHealthcare.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl  {

    @Autowired
    private DoctorRepository doctorRepository;

    public  List<Doctor>getAllDoctor(){
        return doctorRepository.findAll();
    }
}

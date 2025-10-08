package com.SmartHealthcare.repository;

import com.SmartHealthcare.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {

    // Custom query with min and max experience
    @Query("SELECT d FROM Doctor d WHERE " +
            "(:specialization IS NULL OR d.specialization = :specialization) AND " +
            "(:location IS NULL OR d.location = :location) AND " +
            "(:language IS NULL OR d.language = :language) AND " +
            "(:minExp IS NULL OR d.experience >= :minExp) AND " +
            "(:maxExp IS NULL OR d.experience <= :maxExp)")
    List<Doctor> searchDoctors(
            @Param("specialization") String specialization,
            @Param("location") String location,
            @Param("language") String language,
            @Param("minExp") Integer minExp,
            @Param("maxExp") Integer maxExp
    );


    @Query("SELECT d FROM Doctor d WHERE " +
            "(:availability IS NULL OR d.availability = :availability) AND " +
            "(:consultationFees IS NULL OR d.consultationFees = :consultationFees) AND " +
            "(:ratings IS NULL OR d.ratings = :ratings)")
    List<Doctor> filterDoctors(
            @Param("availability") String availability,
            @Param("consultationFees") Double consultationFees,
            @Param("ratings") Double ratings
    );
}

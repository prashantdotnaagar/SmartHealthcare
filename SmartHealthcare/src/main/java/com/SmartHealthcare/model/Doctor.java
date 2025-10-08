package com.SmartHealthcare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Doctor {

    @Id
    private Long doctorId;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private String specialization;

    private String licenseNumber;

    private String phone;

    private String  qualifications;
    private String  location;
    private String  language;
    private Integer     experience;
    private String  availability;
    private Double   consultationFees;

    @DecimalMin(value = "0.0", message = "Rating cannot be less than 0.0")
    @DecimalMax(value = "5.0", message = "Rating cannot be more than 5.0")
    @Column(precision = 2, scale = 1)
    private BigDecimal ratings;
    private String  reviews;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public String getFullName() {
        return firstName + " " + lastName;
    }
}

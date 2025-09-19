package com.SmartHealthcare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    private String specialization;

    private String licenseNumber;

    private String phone;

    @CreationTimestamp
    @Column( nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column( nullable = false)
    private LocalDateTime updatedAt;


    private String getFullName(){
        return firstName+" "+ lastName;
    }
}

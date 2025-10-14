package com.SmartHealthcare.model.admin;

import com.SmartHealthcare.model.user.User;
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
public class Admin {
    @Id
    private Long adminId;

    @OneToOne
    @MapsId
    @JoinColumn(name ="userId")
    @JsonBackReference
    private User user;

    private String department;

    private Integer accessLevel;

    @Column (nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Column (nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String firstName;

    private String lastName;
}

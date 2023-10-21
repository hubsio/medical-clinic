package com.example.demo.model.entity;

import com.example.demo.model.dto.CreateDoctorCommandDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;
    private String specialization;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToMany(mappedBy = "doctors")
    private List<HealthcareFacility> healthcareFacilities;


    public static Doctor create(CreateDoctorCommandDTO createDoctorCommand) {
        return Doctor.builder()
                .firstName(createDoctorCommand.getFirstName())
                .lastName(createDoctorCommand.getLastName())
                .phoneNumber(createDoctorCommand.getPhoneNumber())
                .birthday(createDoctorCommand.getBirthday())
                .specialization(createDoctorCommand.getSpecialization())
                .user(User.builder()
                        .email(createDoctorCommand.getEmail())
                        .password(createDoctorCommand.getPassword())
                        .username(createDoctorCommand.getUsername())
                        .build())
                .build();

    }
}

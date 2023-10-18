package com.example.demo.model.entity;

import com.example.demo.model.dto.CreatePatientCommandDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public static Patient create(CreatePatientCommandDTO createPatientCommand) {
        return Patient.builder()
                .birthday(createPatientCommand.getBirthday())
                .firstName(createPatientCommand.getFirstName())
                .lastName(createPatientCommand.getLastName())
                .idCardNo(createPatientCommand.getIdCardNo())
                .phoneNumber(createPatientCommand.getPhoneNumber())
                .user(User.builder()
                        .email(createPatientCommand.getEmail())
                        .password(createPatientCommand.getPassword())
                        .username(createPatientCommand.getUsername())
                        .build())
                .build();

    }

}

package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditDoctorCommandDTO {
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String specialization;
}

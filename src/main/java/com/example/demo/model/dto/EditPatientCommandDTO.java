package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
public class EditPatientCommandDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
}

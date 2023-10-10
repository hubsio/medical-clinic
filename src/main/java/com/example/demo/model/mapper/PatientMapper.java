package com.example.demo.model.mapper;

import com.example.demo.model.Patient;
import com.example.demo.model.dto.PatientDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatientMapper {
    public static PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setEmail(patient.getEmail());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setPhoneNumber(patient.getPhoneNumber());
        return dto;
    }
    public static Patient convertToEntity(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setEmail(patientDTO.getEmail());
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        return patient;
    }
}

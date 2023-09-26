package com.example.demo.service;


import com.example.demo.exception.InvalidEmailException;
import com.example.demo.exception.PatientNotFoundException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final List<Patient> patients;

    public List<Patient> getAllPatients() {
        return patients;
    }

    public Patient getPatientByEmail(String email) {
        return findPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with the provided email does not exist"));
    }

    public Patient addNewPatient(@RequestBody Patient patient) {
        String email = patient.getEmail();
        if (email != null && !email.isEmpty()) {
            throw new InvalidEmailException("Invalid email address");
        }
        Optional<Patient> existingPatient = findPatientByEmail(email);
        if (existingPatient.isPresent()) {
            throw new UserAlreadyExistsException("User with the provided email already exists");
        }

        patients.add(patient);
        return patient;
    }

    public void deletePatientByEmail(String email) {
        Patient patientToDelete = findPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with the provided email does not exist"));

        patients.remove(patientToDelete);
    }

    public Patient editPatientByEmail(String email, Patient editedPatient) {
        Patient existingPatient = findPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with given email does not exist"));
        existingPatient.setFirstName(editedPatient.getFirstName());
        existingPatient.setLastName(editedPatient.getLastName());
        existingPatient.setPassword(editedPatient.getPassword());
        existingPatient.setPhoneNumber(editedPatient.getPhoneNumber());
        return editedPatient;
    }

    public String updatePassword(@PathVariable String email, @RequestBody String newPassword) {
        Patient existingPatient = findPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with given email does not exist"));

        if (newPassword == null || newPassword.isEmpty()) {
            throw new InvalidEmailException("New password cannot be empty");
        }
        existingPatient.setPassword(newPassword);

        return newPassword;
    }

    private Optional<Patient> findPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }
}


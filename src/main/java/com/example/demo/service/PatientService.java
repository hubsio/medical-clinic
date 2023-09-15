package com.example.demo.service;

import com.example.demo.controller.PatientController;
import com.example.demo.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final List<Patient> patients;

    public List<Patient> getAllPatients() {
        return patients;
    }

    public Patient getPatientByEmail(String email) {
        Optional<Patient> findPatient = findPatientByEmail(email);

        if (findPatient.isEmpty()) {
            throw new RuntimeException("Patient with applied email is not exciting");
        }
        return findPatient.get();
    }

    @PostMapping
    public Patient addNewPatient(@RequestBody Patient patient) {
        String email = patient.getEmail();
        if (email != null && !email.isEmpty()) {
            throw new RuntimeException("Invalid email address");
        }
        Optional<Patient> existingPatient = findPatientByEmail(email);
        if (existingPatient.isPresent()) {
            throw new RuntimeException("User with the provided email already exists");
        }

        patients.add(patient);
        return patient;
    }


    public void deletePatientByEmail(String email) {
        Optional<Patient> patientToDelete = findPatientByEmail(email);

        if (patientToDelete.isEmpty()) {
            throw new RuntimeException("Patient with the provided email does not exist");
        }

        patients.remove(patientToDelete.get());
    }


    public Patient editPatientByEmail(String email, Patient editedPatient) {
        Optional<Patient> existingPatient = findPatientByEmail(email);
        if (existingPatient.isEmpty()) {
            throw new IllegalArgumentException("Patient with given email is not exists");
        }
        existingPatient.get().setFirstName(editedPatient.getFirstName());
        existingPatient.get().setLastName(editedPatient.getLastName());
        existingPatient.get().setPassword(editedPatient.getPassword());
        existingPatient.get().setPhoneNumber(editedPatient.getPhoneNumber());
        return editedPatient;
    }

    @PatchMapping("/{email}/password")
    public String updatePassword(@PathVariable String email, @RequestBody String newPassword) {
        Patient existingPatient = findPatientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist"));

        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
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


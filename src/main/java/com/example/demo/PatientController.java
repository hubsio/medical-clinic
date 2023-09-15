package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final List<Patient> patients;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patients;
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
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

        patients.add(patient);
        return patient;
    }

    @DeleteMapping("/{email}")
    public void deletePatientByEmail(@PathVariable String email) {
        Optional<Patient> patientToDelete = findPatientByEmail(email);

        if (patientToDelete.isEmpty()) {
            throw new RuntimeException("Patient with the provided email does not exist");
        }

        patients.remove(patientToDelete);
    }

    @PutMapping("/{email}")
    public Patient editPatientByEmail(@PathVariable String email, @RequestBody Patient editedPatient) {
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
        Optional<Patient> existingPatient = findPatientByEmail(email);
        if (existingPatient.isEmpty()) {
            throw new IllegalArgumentException("Patient with given email does not exist");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }
        existingPatient.get().setPassword(newPassword);

        return newPassword;
}


    private Optional<Patient> findPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }
}

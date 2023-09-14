package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
@AllArgsConstructor
public class PatientController {
    private final List<Patient> patients;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patients;
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        Optional<Patient> findPatient = patients.stream()
                .filter(patient -> patient.getEmail() != null && patient.getEmail().equals(email))
                .findFirst();

        if (findPatient.isPresent()) {
            return findPatient.get();
        } else {
            throw new RuntimeException("Patient with applied email is not exciting");
        }
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
        boolean removed = patients.removeIf(patient -> {
            String patientEmail = patient.getEmail();
            return patientEmail != null && patientEmail.equals(email);
        });

        if (!removed) {
            throw new RuntimeException("Patient with the provided email does not exist");
        }
    }

    @PutMapping("/{email}")
    public Patient editPatientByEmail(@PathVariable String email, @RequestBody Patient editedPatient) {
        Patient existingPatient = getPatientByEmail(email);
        existingPatient.setFirstName(editedPatient.getFirstName());
        existingPatient.setLastName(editedPatient.getLastName());
        existingPatient.setPassword(editedPatient.getPassword());
        existingPatient.setPhoneNumber(editedPatient.getPhoneNumber());
        return editedPatient;
    }
}

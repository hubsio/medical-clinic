package com.example.demo;

import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private List<Patient> patients = new ArrayList<>();

    @GetMapping
    public List<Patient> getAllPatients() {
        return patients;
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        Optional<Patient> findPatient = patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
        return findPatient.orElse(null);
    }

    @PostMapping
    public Patient addNewPatient(@RequestBody Patient patient) {
        patients.add(patient);
        return patient;
    }

    @DeleteMapping("/{email}")
    public void delatePatientByEmail(@PathVariable String email) {
        patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    @PutMapping("/{email}")
    public Patient editPatientByEmail(@PathVariable String email, @RequestBody Patient editedPatient) {
        Optional<Patient> patientToEdit = patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();

        if (patientToEdit.isPresent()) {
            patients.remove(patientToEdit.get());
            patients.add(editedPatient);
            return editedPatient;
        } else {
            throw new RuntimeException("Patient whit applied email is not exsisiting");
        }
    }
}

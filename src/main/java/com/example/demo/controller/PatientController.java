package com.example.demo.controller;

import com.example.demo.model.Patient;
import com.example.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    public Patient addNewPatient(@RequestBody Patient patient) {
        return patientService.addNewPatient(patient);
    }

    @DeleteMapping("/{email}")
    public void deletePatientByEmail(@PathVariable String email) {
        patientService.deletePatientByEmail(email);
    }

    @PutMapping("/{email}")
    public Patient editPatientByEmail(@PathVariable String email, @RequestBody Patient editedPatient) {

        return patientService.editPatientByEmail(email, editedPatient);
    }

    @PatchMapping("/{email}/password")
    public String updatePassword(@PathVariable String email, @RequestBody String newPassword) {

        return patientService.updatePassword(email, newPassword);
    }
}

package com.example.demo.controller;

import com.example.demo.model.dto.CreatePatientCommandDTO;
import com.example.demo.model.dto.EditPatientCommandDTO;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<PatientDTO> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public PatientDTO getPatient(@PathVariable Long id) {
        return patientService.getPatient(id);
    }

    @PostMapping
    public PatientDTO addNewPatient(@RequestBody CreatePatientCommandDTO patient) {
        return patientService.addNewPatient(patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/{id}")
    public PatientDTO editPatient(@PathVariable Long id, @RequestBody EditPatientCommandDTO editedPatient) {
        return patientService.editPatientById(id, editedPatient);
    }

    @PatchMapping("/{id}/password")
    public String updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        return patientService.updatePassword(id, newPassword);
    }
}

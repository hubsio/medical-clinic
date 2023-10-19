package com.example.demo.controller;

import com.example.demo.model.dto.CreateDoctorCommandDTO;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.EditDoctorCommandDTO;
import com.example.demo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public DoctorDTO getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @PostMapping
    public DoctorDTO addNewDoctor(@RequestBody CreateDoctorCommandDTO doctor) {
        return doctorService.addNewDoctor(doctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @PutMapping("/{id}")
    public DoctorDTO editDoctor(@PathVariable Long id, @RequestBody EditDoctorCommandDTO editedDoctor) {
        return doctorService.editDoctorById(id, editedDoctor);
    }

    @PatchMapping("/{id}/password")
    public String updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        return doctorService.updatePassword(id, newPassword);
    }
}

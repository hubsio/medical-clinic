package com.example.demo.controller;

import com.example.demo.model.dto.HealthcareFacilityDTO;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.service.HealthcareFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/healthcare-facilities")
@RequiredArgsConstructor
public class HealthcareFacilityController {
    public final HealthcareFacilityService healthcareFacilityService;

    @GetMapping
    public List<HealthcareFacilityDTO> getAllFacilities() {
        return healthcareFacilityService.getAllFacilities();
    }

    @GetMapping("/{id}")
    public HealthcareFacilityDTO getFacility(@PathVariable Long id) {
        return healthcareFacilityService.getFacility(id);
    }

    @PostMapping
    public ResponseEntity<HealthcareFacilityDTO> createFacility(@RequestBody HealthcareFacilityDTO facilityDTO) {
        HealthcareFacilityDTO createdFacility = healthcareFacilityService.createHealthcareFacility(facilityDTO);
        return ResponseEntity.ok(createdFacility);
    }

    @PutMapping("/{facilityId}/add-doctor/{doctorId}")
    public void addDoctorToFacility(@PathVariable Long facilityId, @PathVariable Long doctorId) {
        healthcareFacilityService.addDoctorToFacility(facilityId, doctorId);
    }
}

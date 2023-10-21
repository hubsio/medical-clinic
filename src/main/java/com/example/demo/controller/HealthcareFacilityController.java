package com.example.demo.controller;

import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.service.HealthcareFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/healthcare-facilities")
@RequiredArgsConstructor
public class HealthcareFacilityController {
    public final HealthcareFacilityService healthcareFacilityService;

    @GetMapping
    public List<HealthcareFacility> getAllFacilities() {
        return healthcareFacilityService.getAllFacilities();
    }

    @GetMapping("/{id}")
    public HealthcareFacility getFacility(@PathVariable Long id) {
        return healthcareFacilityService.getFacility(id);
    }

    @PostMapping
    public HealthcareFacility createFacility(@RequestBody HealthcareFacility facility) {
        return healthcareFacilityService.createHealthcareFacility(facility);
    }

    @PutMapping("/{facilityId}/add-doctor/{doctorId}")
    public void addDoctorToFacility(@PathVariable Long facilityId, @PathVariable Long doctorId) {
        healthcareFacilityService.addDoctorToFacility(facilityId, doctorId);
    }
}

package com.example.demo.controller;

import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.HealthcareFacilityDTO;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.service.HealthcareFacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/healthcare-facilities")
@RequiredArgsConstructor
public class HealthcareFacilityController {
    public final HealthcareFacilityService healthcareFacilityService;

    @Operation(summary = "Get all healthcare facilities", tags = "Healthcare Facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = HealthcareFacilityDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @GetMapping
    public List<HealthcareFacilityDTO> getAllFacilities() {
        return healthcareFacilityService.getAllFacilities();
    }

    @Operation(summary = "Get healthcare facility by ID", tags = "Healthcare Facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = HealthcareFacilityDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Healthcare Facility not found", content = @Content(schema = @Schema(implementation = String.class, description = "Healthcare Facility not found")))
    })
    @GetMapping("/{id}")
    public HealthcareFacilityDTO getFacility(@PathVariable Long id) {
        return healthcareFacilityService.getFacility(id);
    }

    @Operation(summary = "Create a new healthcare facility", tags = "Healthcare Facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = HealthcareFacilityDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @PostMapping
    public ResponseEntity<HealthcareFacilityDTO> createFacility(@RequestBody HealthcareFacilityDTO facilityDTO) {
        HealthcareFacilityDTO createdFacility = healthcareFacilityService.createHealthcareFacility(facilityDTO);
        return ResponseEntity.ok(createdFacility);
    }

    @Operation(summary = "Add a doctor to a healthcare facility", tags = "Healthcare Facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = HealthcareFacilityDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data or Doctor not found", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Healthcare Facility not found", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))

    })
    @PutMapping("/{facilityId}/add-doctor/{doctorId}")
    public void addDoctorToFacility(@PathVariable Long facilityId, @PathVariable Long doctorId) {
        healthcareFacilityService.addDoctorToFacility(facilityId, doctorId);
    }
}

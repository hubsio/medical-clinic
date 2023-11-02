package com.example.demo.controller;

import com.example.demo.model.dto.*;
import com.example.demo.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @Operation(summary = "Get all patients", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PatientDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @GetMapping
    public List<PatientDTO> getAllPatients() {
        return patientService.getAllPatients();
    }

    @Operation(summary = "Get patient by ID", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Patient with the provided ID does not exist", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @GetMapping("/{id}")
    public PatientDTO getPatient(@PathVariable Long id) {
        return patientService.getPatient(id);
    }

    @Operation(summary = "Add a new patient", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data or Invalid email address or user already exists", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
//            @ApiResponse(responseCode = "400", description = "Invalid email address", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
//            @ApiResponse(responseCode = "400", description = "User with the provided email already exists", content = @Content(schema = @Schema(implementation = String.class, description = "User exists")))
    })
    @PostMapping
    public PatientDTO addNewPatient(@RequestBody CreatePatientCommandDTO patient) {
        return patientService.addNewPatient(patient);
    }

    @Operation(summary = "Delete patient", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Patient with the provided ID does not exist")
    })
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @Operation(summary = "Edit patient", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data and cannot be null", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Patient with the provided ID does not exist", content = @Content(schema = @Schema(implementation = String.class, description = "Patient not found")))
    })
    @PutMapping("/{id}")
    public PatientDTO editPatient(@PathVariable Long id, @RequestBody EditPatientCommandDTO editedPatient) {
        return patientService.editPatientById(id, editedPatient);
    }

    @Operation(summary = "Update patient password", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data and cannot be null or password cannot be empty", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Patient with the provided ID does not exist", content = @Content(schema = @Schema(implementation = String.class, description = "Patient not found")))
    })
    @PatchMapping("/{id}/password")
    public String updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        return patientService.updatePassword(id, newPassword);
    }
}

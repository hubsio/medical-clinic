package com.example.demo.controller;

import com.example.demo.model.dto.CreateDoctorCommandDTO;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.EditDoctorCommandDTO;
import com.example.demo.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Get all doctors", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DoctorDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @GetMapping
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @Operation(summary = "Get doctor by ID", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Doctor with the provided ID does not exist", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid doctor ID")))
    })
    @GetMapping("/{id}")
    public DoctorDTO getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @Operation(summary = "Add new doctor", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request - User with the provided email already exists", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))
    })
    @PostMapping
    public DoctorDTO addNewDoctor(@RequestBody CreateDoctorCommandDTO doctor) {
        return doctorService.addNewDoctor(doctor);
    }

    @Operation(summary = "Delete doctor", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Doctor with the provided ID does not exist")
    })
    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @Operation(summary = "Edit a doctor by ID", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data or Doctor with the provided ID does not exist", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Bad request - Data cannot be null", content = @Content(schema = @Schema(implementation = String.class, description = "Doctor not found")))
    })
    @PutMapping("/{id}")
    public DoctorDTO editDoctor(@PathVariable Long id, @RequestBody EditDoctorCommandDTO editedDoctor) {
        return doctorService.editDoctorById(id, editedDoctor);
    }

    @Operation(summary = "Update Doctor password", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data or empty password cannot be empty", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Not Found - Doctor with the provided ID does not exist", content = @Content(schema = @Schema(implementation = String.class, description = "Doctor not found")))
    })
    @PatchMapping("/{id}/password")
    public String updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        return doctorService.updatePassword(id, newPassword);
    }
}

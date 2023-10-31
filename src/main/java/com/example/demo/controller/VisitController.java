package com.example.demo.controller;

import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.DoctorMapper;
import com.example.demo.model.mapper.VisitMapper;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import com.example.demo.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @Operation(summary = "Add an available visit", tags = "Visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = VisitDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "400", description = "Cannot create a visit for a past date.", content = @Content(schema = @Schema(implementation = String.class, description = "Wrong visit schedule"))),
            @ApiResponse(responseCode = "400", description = "Visit must start and end at quarter past the hour.", content = @Content(schema = @Schema(implementation = String.class, description = "Wrong visit schedule"))),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(schema = @Schema(implementation = String.class, description = "Doctor not found"))),
            @ApiResponse(responseCode = "404", description = "Healthcare Facility not found", content = @Content(schema = @Schema(implementation = String.class, description = "Healthcare Facility not found")))
    })
    @PostMapping
    public VisitDTO addAvailableVisit(@RequestBody CreateVisitCommand createVisitCommand) {
        return visitService.addAvailableVisit(createVisitCommand);
    }

    @Operation(summary = "Book a visit", tags = "Visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = VisitDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data"))),
            @ApiResponse(responseCode = "404", description = "Visit not found", content = @Content(schema = @Schema(implementation = String.class, description = "Visit not found"))),
            @ApiResponse(responseCode = "400", description = "Cannot book a visit for a past date.", content = @Content(schema = @Schema(implementation = String.class, description = "Wrong visit schedule"))),
            @ApiResponse(responseCode = "400", description = "Visit is not available for booking.", content = @Content(schema = @Schema(implementation = String.class, description = "Wrong visit schedule"))),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(schema = @Schema(implementation = String.class, description = "Patient not found"))),
            @ApiResponse(responseCode = "404", description = "Healthcare Facility not found", content = @Content(schema = @Schema(implementation = String.class, description = "Healthcare Facility not found"))),
            @ApiResponse(responseCode = "400", description = "Patient already has a visit within the specified time range.", content = @Content(schema = @Schema(implementation = String.class, description = "The same visit")))
    })
    @PostMapping("/book")
    public VisitDTO bookVisit(@RequestBody BookVisitRequest bookVisitRequest) {
        return visitService.bookVisit(bookVisitRequest);
    }

    @Operation(summary = "Get patient visits", tags = "Visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = VisitDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))

    })
    @GetMapping("/patient/{patientId}")
    public List<VisitDTO> getPatientVisits(@PathVariable Long patientId) {
        return visitService.getPatientVisits(patientId);
    }

    @Operation(summary = "Get doctor visits", tags = "Visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = VisitDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = String.class, description = "Invalid request data")))

    })
    @GetMapping("/doctor/{doctorId}")
    public List<VisitDTO> getDoctorVisits(@PathVariable Long doctorId) {
        return visitService.getDoctorVisits(doctorId);
    }
}

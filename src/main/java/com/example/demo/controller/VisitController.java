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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;
    @PostMapping
    public VisitDTO addAvailableVisit(@RequestBody CreateVisitCommand createVisitCommand) {
        return visitService.addAvailableVisit(createVisitCommand);
    }

    @PostMapping("/book")
    public VisitDTO bookVisit(@RequestBody BookVisitRequest bookVisitRequest) {
        return visitService.bookVisit(bookVisitRequest);
    }

    @GetMapping("/patient/{patientId}")
    public List<VisitDTO> getPatientVisits(@PathVariable Long patientId) {
        return visitService.getPatientVisits(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<VisitDTO> getDoctorVisits(@PathVariable Long doctorId) {
        return visitService.getDoctorVisits(doctorId);
    }
}

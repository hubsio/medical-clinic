package com.example.demo.controller;

import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.DoctorMapper;
import com.example.demo.service.DoctorService;
import com.example.demo.service.VisitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/visits")
public class VisitController {
    private VisitService visitService;
    private DoctorService doctorService;
    private DoctorMapper doctorMapper;

    @PostMapping("/add")
    public Visit addAvailableVisit(@RequestBody DoctorDTO doctorDTO, @RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime) {
        Doctor doctor = doctorMapper.doctorDtoToDoctor(doctorDTO);
        return visitService.addAvailableVisit(doctor, startDateTime, endDateTime);
    }
}

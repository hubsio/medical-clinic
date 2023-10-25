package com.example.demo.service;

import com.example.demo.exception.*;
import com.example.demo.model.VisitType;
import com.example.demo.model.dto.BookVisitRequest;
import com.example.demo.model.dto.CreateVisitCommand;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.DoctorMapper;
import com.example.demo.model.mapper.PatientMapper;
import com.example.demo.model.mapper.VisitMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VisitServiceTest {
    DoctorRepository doctorRepository;
    HealthcareFacilityRepository healthcareFacilityRepository;
    PatientRepository patientRepository;
    VisitRepository visitRepository;
    VisitMapper visitMapper;
    VisitService visitService;

    @BeforeEach
    void setUp() {
        this.doctorRepository = mock(DoctorRepository.class);
        this.healthcareFacilityRepository = mock(HealthcareFacilityRepository.class);
        this.patientRepository = mock(PatientRepository.class);
        this.visitRepository = mock(VisitRepository.class);
        this.visitMapper = mock(VisitMapper.class);
        this.visitService = new VisitService(visitRepository, doctorRepository, visitMapper, healthcareFacilityRepository, patientRepository);
    }

    @Test
    void addAvailableVisit_ValidInput_ShouldAddVisit() {
        CreateVisitCommand command = new CreateVisitCommand(1L, 1L, LocalDateTime.of(2024, 11, 10, 14, 0), LocalDateTime.of(2024, 11, 10, 14, 15), 200.00);
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        HealthcareFacility healthcareFacility = new HealthcareFacility();
        healthcareFacility.setId(2L);
        List<Visit> overlappingVisits = new ArrayList<>();
        Visit savedVisit = new Visit();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(healthcareFacilityRepository.findById(2L)).thenReturn(Optional.of(healthcareFacility));
        when(visitRepository.findAllOverlapping(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(overlappingVisits);
        when(visitMapper.visitToVisitDto(any(Visit.class))).thenReturn(new VisitDTO());
        when(visitRepository.save(any(Visit.class))).thenReturn(savedVisit);

        VisitDTO result = visitService.addAvailableVisit(command);

        assertEquals(1L, result.getDoctorId());
        assertEquals(2L, result.getHealthcareFacilityId());
        assertEquals(VisitType.CREATED, result.getVisitType());
    }

    @Test
    void bookVisit_ValidBooking_ShouldBookVisit() {
        BookVisitRequest request = new BookVisitRequest();
        request.setVisitId(1L);
        request.setPatientId(2L);

        Visit visit = new Visit();
        visit.setId(1L);
        visit.setStartDateTime(LocalDateTime.now().plusHours(1));
        visit.setVisitType(VisitType.CREATED);

        Patient patient = new Patient();
        patient.setId(2L);

        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));
        when(patientRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(visitRepository.existsByPatientAndStartDateTimeBetweenAndVisitType(eq(patient), any(), any(), eq(VisitType.SCHEDULED))).thenReturn(false);

        VisitDTO result = visitService.bookVisit(request);

        assertEquals(VisitType.SCHEDULED, visit.getVisitType());
        assertEquals(patient, visit.getPatient());

        assertEquals(1L, result.getVisitId());
        assertEquals(2L, result.getDoctorId());
    }

    @Test
    void getPatientVisits_ValidInput_ShouldReturnPatientVisits() {
        Long patientId = 1L;
        Visit visit1 = new Visit();
        visit1.setId(1L);
        Patient patient1 = new Patient();
        patient1.setId(patientId);
        visit1.setPatient(patient1);

        Visit visit2 = new Visit();
        visit2.setId(2L);

        List<Visit> allVisits = Arrays.asList(visit1, visit2);

        when(visitRepository.findAll()).thenReturn(allVisits);
        when(visitMapper.visitToVisitDto(visit1)).thenReturn(new VisitDTO(1L, null, null, null, null, null, null, null));
        when(visitMapper.visitToVisitDto(visit2)).thenReturn(new VisitDTO(2L, null, null, null, null, null, null, null));

        List<VisitDTO> patientVisits = visitService.getPatientVisits(patientId);

        assertEquals(1, patientVisits.size());
        assertEquals(1L, patientVisits.get(0).getId());
    }

    @Test
    void getDoctorVisits_ValidInput_ShouldReturnDoctorVisits() {
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        Visit visit1 = new Visit();
        visit1.setId(1L);
        visit1.setDoctor(doctor);
        Doctor doctor1 = new Doctor();
        doctor1.setId(2L);
        Visit visit2 = new Visit();
        visit2.setId(2L);
        visit2.setDoctor(doctor1);

        List<Visit> doctorVisits = Arrays.asList(visit1, visit2);
        when(visitRepository.findAll()).thenReturn(doctorVisits);

        VisitDTO visitDTO1 = new VisitDTO(1L, null, null, null, null, null, null, null);
        VisitDTO visitDTO2 = new VisitDTO(2L, null, null, null, null, null, null, null);

        when(visitMapper.visitToVisitDto(visit1)).thenReturn(visitDTO1);
        when(visitMapper.visitToVisitDto(visit2)).thenReturn(visitDTO2);

        List<VisitDTO> result = visitService.getDoctorVisits(doctorId);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void addAvailableVisit_PastDate_ShouldThrowException() {
        CreateVisitCommand command = new CreateVisitCommand();
        command.setStartDateTime(LocalDateTime.of(2022, 1, 1, 12, 0)); // Data w przeszłości

        IllegalVisitOperationException exception = assertThrows(IllegalVisitOperationException.class, () -> visitService.addAvailableVisit(command));
        assertEquals("Cannot create a visit for a past date.", exception.getMessage());
    }

    @Test
    void addAvailableVisit_InvalidTime_ShouldThrowException() {
        CreateVisitCommand command = new CreateVisitCommand();
        command.setStartDateTime(LocalDateTime.of(2024, 1, 1, 12, 31)); // Minuta nie jest podzielna przez 15
        command.setEndDateTime(LocalDateTime.of(2024, 1, 1, 12, 44)); // Minuta nie jest podzielna przez 15

        IllegalVisitOperationException exception = assertThrows(IllegalVisitOperationException.class, () -> visitService.addAvailableVisit(command));
        assertEquals("Visit must start and end at quarter past the hour.", exception.getMessage());
    }
    ///////////
    @Test
    void bookVisit_VisitNotFound_ShouldThrowVisitNotFoundException() {
        BookVisitRequest request = new BookVisitRequest();
        request.setPatientId(1L);
        request.setVisitId(1L);

        VisitNotFoundException exception = assertThrows(VisitNotFoundException.class, () -> visitService.bookVisit(request));
        assertEquals("Visit not found", exception.getMessage());
    }
}

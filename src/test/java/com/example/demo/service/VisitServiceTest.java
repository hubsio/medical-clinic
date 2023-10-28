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
    void testAddAvailableVisit() {
        CreateVisitCommand command = new CreateVisitCommand();
        command.setDoctorId(1L);
        command.setHealthcareFacilityId(2L);
        command.setStartDateTime(LocalDateTime.of(2023,12,12,14,0));
        command.setEndDateTime(LocalDateTime.of(2023,12,12,14,15));
        command.setPrice(100.0);

        Doctor doctor = new Doctor();
        doctor.setId(1L);

        HealthcareFacility facility = new HealthcareFacility();
        facility.setId(2L);

        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setHealthcareFacility(facility);

        when(doctorRepository.findById(1L)).thenReturn(java.util.Optional.of(doctor));
        when(healthcareFacilityRepository.findById(2L)).thenReturn(java.util.Optional.of(facility));
        when(visitRepository.findAllOverlapping(1L, command.getStartDateTime(), command.getEndDateTime())).thenReturn(new ArrayList<>());
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);
        when(visitMapper.visitToVisitDto(any(Visit.class))).thenReturn(new VisitDTO());

        VisitDTO result = visitService.addAvailableVisit(command);
        result.setDoctorId(1L);
        result.setHealthcareFacilityId(2L);
        result.setStartDateTime(LocalDateTime.of(2023,12,12,14,0));
        result.setEndDateTime(LocalDateTime.of(2023,12,12,14,15));
        result.setPrice(100.0);

        assertNotNull(result);
        assertEquals(1L, result.getDoctorId());
        assertEquals(2L, result.getHealthcareFacilityId());
        assertEquals(command.getStartDateTime(), result.getStartDateTime());
        assertEquals(command.getEndDateTime(), result.getEndDateTime());
        assertEquals(command.getPrice(), result.getPrice());
    }

    @Test
    void testBookVisit() {
        BookVisitRequest request = new BookVisitRequest();
        request.setVisitId(1L);
        request.setPatientId(2L);
        request.setStartDateTime(LocalDateTime.of(2023,12,12,14,0));
        request.setEndDateTime(LocalDateTime.of(2023,12,12,14,15));

        Visit visit = new Visit();
        visit.setId(1L);
        visit.setVisitType(VisitType.CREATED);
        visit.setStartDateTime(LocalDateTime.of(2023,12,12,14,0));
        visit.setEndDateTime(LocalDateTime.of(2023,12,12,14,15));

        Patient patient = new Patient();
        patient.setId(2L);

        Mockito.when(visitRepository.findById(1L)).thenReturn(java.util.Optional.of(visit));
        Mockito.when(visitRepository.existsByPatientAndStartDateTimeBetweenAndVisitType(patient, visit.getStartDateTime(), visit.getEndDateTime(), VisitType.SCHEDULED)).thenReturn(false);
        Mockito.when(patientRepository.findById(2L)).thenReturn(java.util.Optional.of(patient));
        Mockito.when(visitRepository.save(any(Visit.class))).thenReturn(visit);
        Mockito.when(visitMapper.visitToVisitDto(any(Visit.class))).thenReturn(new VisitDTO());

        VisitDTO result = visitService.bookVisit(request);
        result.setVisitId(1L);
        result.setStartDateTime(LocalDateTime.of(2023,12,12,14,0));
        result.setEndDateTime(LocalDateTime.of(2023,12,12,14,15));

        assertNotNull(result);
        assertEquals(1L, result.getVisitId());
        assertEquals(request.getStartDateTime(), result.getStartDateTime());
        assertEquals(request.getEndDateTime(), result.getEndDateTime());
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
    void addAvailableVisit_TimeNotRoundedTo15_ShouldThrowException() {
        CreateVisitCommand command = new CreateVisitCommand();
        command.setStartDateTime(LocalDateTime.of(2024, 1, 1, 12, 31));
        command.setEndDateTime(LocalDateTime.of(2024, 1, 1, 12, 44));

        IllegalVisitOperationException exception = assertThrows(IllegalVisitOperationException.class, () -> visitService.addAvailableVisit(command));
        assertEquals("Visit must start and end at quarter past the hour.", exception.getMessage());
    }

    @Test
    void testAddAvailableVisit_DoctorNotFound() {
        CreateVisitCommand command = new CreateVisitCommand(null, 1L, LocalDateTime.of(2023, 12, 12, 14, 0), LocalDateTime.of(2023, 12, 12, 14, 15), 250.00);

        when(doctorRepository.findById(command.getDoctorId())).thenReturn(Optional.empty());

        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class, () -> visitService.addAvailableVisit(command));
        assertEquals("Doctor not found", exception.getMessage());
    }

    @Test
    void testAddAvailableVisit_HealthcareFacilityNotFound() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        CreateVisitCommand command = new CreateVisitCommand(doctor.getId(), null, LocalDateTime.of(2023, 12, 12, 14, 0), LocalDateTime.of(2023, 12, 12, 14, 15), 250.00);
        when(doctorRepository.findById(command.getDoctorId())).thenReturn(Optional.of(doctor));
        when(healthcareFacilityRepository.findById(command.getHealthcareFacilityId())).thenReturn(Optional.empty());

        HealthcareFacilityNotFoundException exception = assertThrows(HealthcareFacilityNotFoundException.class, () -> visitService.addAvailableVisit(command));
        assertEquals("Healthcare Facility not found", exception.getMessage());
    }

    @Test
    void bookVisit_VisitNotFound_ShouldThrowVisitNotFoundException() {
        BookVisitRequest request = new BookVisitRequest();
        request.setPatientId(1L);
        request.setVisitId(1L);

        VisitNotFoundException exception = assertThrows(VisitNotFoundException.class, () -> visitService.bookVisit(request));
        assertEquals("Visit not found", exception.getMessage());
    }

    @Test
    void testBookVisit_IllegalVisitOperationException_CannotBookAPastDate() {

        BookVisitRequest request = new BookVisitRequest();
        request.setVisitId(1L);
        request.setPatientId(1L);

        Visit visit = new Visit();
        visit.setStartDateTime(LocalDateTime.of(2022, 1, 1, 10, 0));
        visit.setEndDateTime(LocalDateTime.of(2022, 1, 1, 10, 15));
        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));

        IllegalVisitOperationException exception = assertThrows(IllegalVisitOperationException.class, () -> visitService.bookVisit(request));
        assertEquals("Cannot book a visit for a past date.", exception.getMessage());
    }

    @Test
    void testBookVisit_IllegalVisitOperationException_VisitNotAvailableForBooking() {
        BookVisitRequest request = new BookVisitRequest();
        request.setVisitId(1L);
        request.setPatientId(1L);

        Visit visit = new Visit();
        visit.setVisitType(VisitType.SCHEDULED);
        visit.setStartDateTime(LocalDateTime.of(2024, 12, 12, 14, 0));
        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));

        IllegalVisitOperationException exception = assertThrows(IllegalVisitOperationException.class, () -> visitService.bookVisit(request));
        assertEquals("Visit is not available for booking.", exception.getMessage());
    }

    @Test
    void testBookVisit_PatientConflict_IllegalVisitOperationException_PatientHasVisitWithinTimeRange() {
        BookVisitRequest request = new BookVisitRequest();
        request.setVisitId(1L);
        request.setPatientId(1L);

        Visit visit = new Visit();
        visit.setVisitType(VisitType.CREATED);
        visit.setStartDateTime(LocalDateTime.of(2023, 12, 12, 14, 0));
        visit.setEndDateTime(LocalDateTime.of(2023, 12, 12, 14, 15));
        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));

        Patient patient = new Patient();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(visitRepository.existsByPatientAndStartDateTimeBetweenAndVisitType(patient, visit.getStartDateTime(), visit.getEndDateTime(), VisitType.SCHEDULED)).thenReturn(true);

        IllegalVisitOperationException exception = assertThrows(IllegalVisitOperationException.class, () -> visitService.bookVisit(request));

        assertEquals("Patient already has a visit within the specified time range.", exception.getMessage());
    }
}

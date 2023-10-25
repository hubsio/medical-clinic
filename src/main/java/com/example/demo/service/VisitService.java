package com.example.demo.service;

import com.example.demo.exception.*;
import com.example.demo.model.VisitType;
import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.VisitMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final VisitMapper visitMapper;
    private final HealthcareFacilityRepository healthcareFacilityRepository;
    private final PatientRepository patientRepository;


    public VisitDTO addAvailableVisit(CreateVisitCommand command) {
        if (command.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalVisitOperationException("Cannot create a visit for a past date.");
        }

        if (command.getStartDateTime().getMinute() % 15 != 0 || command.getEndDateTime().getMinute() % 15 != 0) {
            throw new IllegalVisitOperationException("Visit must start and end at quarter past the hour.");
        }

        Doctor doctor = doctorRepository.findById(command.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        HealthcareFacility healthcareFacility = healthcareFacilityRepository.findById(command.getHealthcareFacilityId())
                .orElseThrow(() -> new HealthcareFacilityNotFoundException("Healthcare Facility not found"));

        List<Visit> overlappingVisits = visitRepository.findAllOverlapping(
                doctor.getId(),
                command.getStartDateTime(),
                command.getEndDateTime()
        );

        if (!overlappingVisits.isEmpty()) {
            throw new IllegalVisitOperationException("New visit overlaps with existing visits.");
        }

        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setStartDateTime(command.getStartDateTime());
        visit.setEndDateTime(command.getEndDateTime());
        visit.setVisitType(VisitType.CREATED);
        visit.setPrice(command.getPrice());
        visit.setHealthcareFacility(healthcareFacility);
        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }

    public VisitDTO bookVisit(BookVisitRequest request) {
        Long visitId = request.getVisitId();
        Long patientId = request.getPatientId();

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        if (visit.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalVisitOperationException("Cannot book a visit for a past date.");
        }

        if (visit.getVisitType() != VisitType.CREATED) {
            throw new IllegalVisitOperationException("Visit is not available for booking.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        LocalDateTime startDateTime = visit.getStartDateTime();
        LocalDateTime endDateTime = visit.getEndDateTime();

        boolean hasConflict = visitRepository.existsByPatientAndStartDateTimeBetweenAndVisitType(patient, startDateTime, endDateTime, VisitType.SCHEDULED);

        if (hasConflict) {
            throw new IllegalVisitOperationException("Patient already has a visit within the specified time range.");
        }

        visit.setVisitType(VisitType.SCHEDULED);
        visit.setPatient(patient);
        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }

    public List<VisitDTO> getPatientVisits(Long patientId) {
        List<Visit> allVisits = visitRepository.findAll();
        return allVisits.stream()
                .filter(visit -> visit.getPatient() != null && visit.getPatient().getId().equals(patientId))
                .map(visitMapper::visitToVisitDto)
                .collect(Collectors.toList());
    }

    public List<VisitDTO> getDoctorVisits(Long doctorId) {
        List<Visit> doctorVisits = visitRepository.findAll();
        return doctorVisits.stream()
                .filter(visit -> visit.getDoctor() != null && visit.getDoctor().getId().equals(doctorId))
                .map(visitMapper::visitToVisitDto)
                .collect(Collectors.toList());
    }
}

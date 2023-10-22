package com.example.demo.service;

import com.example.demo.model.VisitType;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Visit;
import com.example.demo.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;

    public Visit addAvailableVisit(Doctor doctor, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setStartDateTime(startDateTime);
        visit.setEndDateTime(endDateTime);
        visit.setVisitType(VisitType.CREATED);
        return visitRepository.save(visit);
    }
}

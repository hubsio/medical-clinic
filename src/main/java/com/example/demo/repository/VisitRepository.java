package com.example.demo.repository;

import com.example.demo.model.VisitType;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {
    @Query("SELECT v FROM Visit v " +
            "WHERE v.doctor.id = :doctorId " +
            "AND v.startDateTime <= :endDateTime " +
            "AND v.endDateTime >= :startDateTime")
    List<Visit> findAllOverlapping(long doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    boolean existsByPatientAndStartDateTimeBetweenAndVisitType(Patient patient, LocalDateTime start, LocalDateTime end, VisitType visitType);

}

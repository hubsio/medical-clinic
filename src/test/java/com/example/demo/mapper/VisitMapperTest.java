package com.example.demo.mapper;

import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.VisitMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisitMapperTest {
    private final VisitMapper visitMapper = Mappers.getMapper(VisitMapper.class);

    @Test
    void visitToVisitDTO() {
        Visit visit = new Visit();
        visit.setId(1L);
        visit.setDoctor(new Doctor(1L, "Dr. Smith", null, null, null, null, null, null));
        visit.setHealthcareFacility(new HealthcareFacility(1L, "Hospital A", null));

        VisitDTO visitDTO = visitMapper.visitToVisitDto(visit);

        assertEquals(1L, visitDTO.getVisitId());
        assertEquals(1L, visitDTO.getDoctorId());
        assertEquals(1L, visitDTO.getHealthcareFacilityId());
    }

    @Test
    void visitDtoToVisit() {
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setId(1L);
        visitDTO.setDoctorId(1L);
        visitDTO.setHealthcareFacilityId(1L);

        Visit visit = visitMapper.visitDtoToVisit(visitDTO);

        assertEquals(1L, visit.getId());
        assertEquals(1L, visit.getDoctor().getId());
        assertEquals(1L, visit.getHealthcareFacility().getId());
    }
}

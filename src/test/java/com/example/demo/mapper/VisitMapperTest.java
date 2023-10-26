package com.example.demo.mapper;

import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.VisitMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisitMapperTest {
    private final VisitMapper visitMapper = Mappers.getMapper(VisitMapper.class);

    @ParameterizedTest
    @MethodSource("provideVisitsAndVisitDTOs")
    void visitToVisitDTO(Visit visit, VisitDTO expectedVisitDTO) {
        VisitDTO visitDTO = visitMapper.visitToVisitDto(visit);

        assertEquals(expectedVisitDTO.getVisitId(), visitDTO.getVisitId());
        assertEquals(expectedVisitDTO.getDoctorId(), visitDTO.getDoctorId());
        assertEquals(expectedVisitDTO.getHealthcareFacilityId(), visitDTO.getHealthcareFacilityId());
    }

    @Test
    void shouldMapVisitDTOToVisit() {
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setVisitId(1L);
        visitDTO.setDoctorId(2L);
        visitDTO.setHealthcareFacilityId(3L);
        visitDTO.setStartDateTime(LocalDateTime.of(2023, 12, 12, 14, 0));
        visitDTO.setEndDateTime(LocalDateTime.of(2023, 12, 12, 14, 15));
        visitDTO.setPrice(250.00);

        Visit visit = visitMapper.visitDtoToVisit(visitDTO);
        visit.setStartDateTime(LocalDateTime.of(2023, 12, 12, 14, 0));
        visit.setEndDateTime(LocalDateTime.of(2023, 12, 12, 14, 15));
        visit.setPrice(250.00);

        assertEquals(visitDTO.getStartDateTime(), visit.getStartDateTime());
        assertEquals(visitDTO.getEndDateTime(), visit.getEndDateTime());
        assertEquals(visitDTO.getPrice(), visit.getPrice());
    }

    static Stream<Arguments> provideVisitsAndVisitDTOs() {
        Visit visit1 = new Visit();
        visit1.setId(1L);
        visit1.setDoctor(new Doctor(1L, "Dr. Smith", null, null, null, null, null, null));
        visit1.setHealthcareFacility(new HealthcareFacility(1L, "Hospital A", null));

        VisitDTO visitDTO1 = new VisitDTO();
        visitDTO1.setVisitId(1L);
        visitDTO1.setDoctorId(1L);
        visitDTO1.setHealthcareFacilityId(1L);

        Visit visit2 = new Visit();
        visit2.setId(2L);
        visit2.setDoctor(new Doctor(2L, "Dr. Johnson", null, null, null, null, null, null));
        visit2.setHealthcareFacility(new HealthcareFacility(2L, "Hospital B", null));

        VisitDTO visitDTO2 = new VisitDTO();
        visitDTO2.setVisitId(2L);
        visitDTO2.setDoctorId(2L);
        visitDTO2.setHealthcareFacilityId(2L);

        return Stream.of(
                Arguments.of(visit1, visitDTO1),
                Arguments.of(visit2, visitDTO2)
        );
    }
}

package com.example.demo.model.mapper;

import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitMapper {
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "healthcareFacility.id", target = "healthcareFacilityId")
    @Mapping(source = "id", target = "visitId")
    VisitDTO visitToVisitDto(Visit visit);

    Visit visitDtoToVisit(VisitDTO visitDTO);
}

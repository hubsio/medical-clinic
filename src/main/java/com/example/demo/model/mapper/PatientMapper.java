package com.example.demo.model.mapper;

import com.example.demo.model.entity.Patient;
import com.example.demo.model.dto.PatientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {
    @Mapping(source = "user.email", target = "email")
    PatientDTO patientToPatientDTO(Patient patient);
    Patient patientDtoToPatient(PatientDTO patientDTO);
}

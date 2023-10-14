package com.example.demo.model.mapper;

import com.example.demo.model.Patient;
import com.example.demo.model.dto.PatientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);
    PatientDTO patientToPatientDTO(Patient patient);
    Patient patientDtoToPatient(PatientDTO patientDTO);
}

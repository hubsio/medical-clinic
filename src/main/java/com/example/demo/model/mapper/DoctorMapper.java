package com.example.demo.model.mapper;

import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorMapper {
    @Mapping(source = "user.email", target = "email")
    DoctorDTO doctorToDoctorDTO(Doctor doctor);
    Doctor doctorDtoToDoctor(DoctorDTO doctorDTO);

}

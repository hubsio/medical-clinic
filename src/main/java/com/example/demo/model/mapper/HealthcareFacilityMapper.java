package com.example.demo.model.mapper;

import com.example.demo.model.dto.HealthcareFacilityDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HealthcareFacilityMapper {
    @Mapping(source = "doctors", target = "doctorIds", qualifiedByName = "mapDoctor")
    HealthcareFacilityDTO convertFacilityToDto (HealthcareFacility healthcareFacility);
    @Mapping(source = "doctorIds", target = "doctors", qualifiedByName = "mapDoctorIds")
    HealthcareFacility DTOToFacility(HealthcareFacilityDTO facilityDTO);

    @Named("mapDoctor")
    default List<Long> mapDoctors(List<Doctor> doctors) {
        if (doctors == null) {
            return new ArrayList<>();
        }
        return doctors.stream()
                .map(Doctor::getId)
                .collect(Collectors.toList());
    }

    @Named("mapDoctorIds")
    default List<Doctor> mapDoctorIds(List<Long> doctorIds) {
        if (doctorIds == null) {
            return new ArrayList<>();
        }
        return doctorIds.stream()
                .map(id -> {
                    Doctor doctor = new Doctor();
                    doctor.setId(id);
                    return doctor;
                })
                .collect(Collectors.toList());
    }
}

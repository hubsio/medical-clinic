package com.example.demo.model.dto;

import com.example.demo.model.VisitType;
import com.example.demo.model.entity.Visit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitDTO {
    private Long id;
    private Long doctorId;
    private Double price;
    private Long healthcareFacilityId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private VisitType visitType;
    private Long visitId;
}

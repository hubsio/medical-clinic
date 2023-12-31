package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateVisitCommand {
    private Long doctorId;
    private Long healthcareFacilityId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Double price;
}

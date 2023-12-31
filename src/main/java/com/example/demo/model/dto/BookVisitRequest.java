package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookVisitRequest {
    private Long visitId;
    private Long patientId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

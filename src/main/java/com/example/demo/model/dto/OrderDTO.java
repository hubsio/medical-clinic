package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String customerName;
    private String orderDate;
    private List<ProductDTO> products;
}

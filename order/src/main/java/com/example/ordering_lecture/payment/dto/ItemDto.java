package com.example.ordering_lecture.payment.dto;

import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private int count;
}

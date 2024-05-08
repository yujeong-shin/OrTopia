package com.example.ordering_lecture.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerResponseForEventDto {
    private String companyName;
    private Long eventId;
}

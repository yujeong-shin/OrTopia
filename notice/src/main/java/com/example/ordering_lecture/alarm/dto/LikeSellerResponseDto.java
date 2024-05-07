package com.example.ordering_lecture.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeSellerResponseDto {
    private String sellerEmail;
    private Long eventId;
}

package com.example.ordering_lecture.recommendation.dto;

import lombok.Data;

@Data
public class ReviewData {
    private Long id;
    private Long memberID;
    private Long itemID;
    private byte score;
}
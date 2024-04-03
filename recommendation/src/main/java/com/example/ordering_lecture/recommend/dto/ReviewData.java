package com.example.ordering_lecture.recommend.dto;

import lombok.Data;

@Data
public class ReviewData {
    private Long id;
    private Long memberID;
    private Long itemID;
    private byte score;
}

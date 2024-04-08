package com.example.ordering_lecture.recommend.dto;

import com.example.ordering_lecture.recommend.entity.Item;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RecommendationRedisData {
    private Long id;
    private String imagePath;

    public static RecommendationRedisData toDto(Item item){
        return RecommendationRedisData.builder()
                .imagePath(item.getImagePath())
                .build();
    }
}

package com.example.ordering_lecture.item.dto;

import com.example.ordering_lecture.item.entity.ItemOptionQuantity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemOptionQuantityResponseDto {
    private Long id;
    private String value1;
    private String value2;
    private String value3;
    private int quantity;

    public static ItemOptionQuantityResponseDto toDto(ItemOptionQuantity itemOptionQuantity) {
        return ItemOptionQuantityResponseDto.builder()
                .id(itemOptionQuantity.getId())
                .quantity(itemOptionQuantity.getQuantity())
                .value1(itemOptionQuantity.getValue1())
                .value2(itemOptionQuantity.getValue2())
                .value3(itemOptionQuantity.getValue3())
                .build();
    }
}

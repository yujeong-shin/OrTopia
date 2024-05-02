package com.example.ordering_lecture.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellerGraphStockData {
    private String itemName;
    private Long stock;
}
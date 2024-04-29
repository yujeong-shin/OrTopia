package com.example.ordering_lecture.orderdetail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellerGraphItemPriceData {
    private String itemName;
    private Long price;
}

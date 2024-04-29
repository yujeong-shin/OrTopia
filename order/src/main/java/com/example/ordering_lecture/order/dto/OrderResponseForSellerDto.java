package com.example.ordering_lecture.order.dto;

import com.example.ordering_lecture.order.entity.PaymentMethod;
import com.example.ordering_lecture.orderdetail.entity.OrderDetail;
import com.example.ordering_lecture.orderdetail.entity.Statue;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderResponseForSellerDto {
    private Long id;
    private Long orderNumber;
    private LocalDateTime createdTime;
    private String buyerEmail; // 구매자 이메일
    private String recipientName; // 수령인 이름
    private String phoneNumber; // 수령인 번호
    private PaymentMethod PaymentMethod;
    private int totalPrice;
    private String itemName; // 상품 이름
    private String options;
    private int quantity;
    private Statue statue;


    public static OrderResponseForSellerDto toDto(OrderDetail orderDetail){
        return OrderResponseForSellerDto.builder()
                .id(orderDetail.getId())
                .buyerEmail(orderDetail.getOrdering().getEmail())
                .orderNumber(orderDetail.getOrdering().getOrderNumber())
                .createdTime(orderDetail.getCreatedTime())
                .recipientName(orderDetail.getOrdering().getRecipientName())
                .phoneNumber(orderDetail.getOrdering().getPhoneNumber())
                .PaymentMethod(orderDetail.getOrdering().getPaymentMethod())
                .totalPrice(orderDetail.getOrdering().getTotalPrice())
                .itemName("")
                .options(orderDetail.getOptions())
                .quantity(orderDetail.getQuantity())
                .statue(orderDetail.getStatue())
                .build();
    }
}

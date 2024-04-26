package com.example.ordering_lecture.order.dto;

import com.example.ordering_lecture.order.entity.Ordering;
import com.example.ordering_lecture.orderdetail.dto.OrderDetailRequestDto;
import com.example.ordering_lecture.order.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private int totalPrice;
    @NotNull
    private Long addressId;
    @NotNull
    private Long buyerId;
    @NotNull
    private Long orderNumber;
    @NotNull
    private String email;
    @NotNull
    private String paymentMethod;
    @NotNull
    private String recipient;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String pgToken;
    private List<OrderDetailRequestDto> orderDetailRequestDtoList;

    public Ordering toEntity(){
        PaymentMethod paymentMethod = null;
        try {
            paymentMethod = PaymentMethod.valueOf(this.paymentMethod);
        }catch (Exception e){
            //TODO : 에러 추후 수정
            e.printStackTrace();
        }
        return Ordering.builder()
                .addressId(this.addressId)
                .buyerId(this.buyerId)
                .orderNumber(this.orderNumber)
                .totalPrice(this.totalPrice)
                .recipient(this.recipient)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .PaymentMethod(paymentMethod)
                .build();
    }
}

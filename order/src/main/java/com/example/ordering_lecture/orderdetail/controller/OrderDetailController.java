package com.example.ordering_lecture.orderdetail.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.order.dto.OrderResponseDto;
import com.example.ordering_lecture.order.dto.OrderUpdateDto;
import com.example.ordering_lecture.orderdetail.dto.OrderDetailResponseDto;
import com.example.ordering_lecture.orderdetail.dto.OrderDetailUpdateDto;
import com.example.ordering_lecture.orderdetail.service.OrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    // 주문 상태 수정
    @PatchMapping("/update")
    public ResponseEntity<OrTopiaResponse> updateOrder(@RequestBody OrderDetailUpdateDto orderDetailUpdateDto){
        OrderDetailResponseDto orderDetailResponseDto = orderDetailService.updateOrder(orderDetailUpdateDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("update success",orderDetailResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
}
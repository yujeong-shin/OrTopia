package com.example.ordering_lecture.orderdetail.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.order.dto.OrderResponseDto;
import com.example.ordering_lecture.order.dto.OrderUpdateDto;
import com.example.ordering_lecture.order.entity.Ordering;
import com.example.ordering_lecture.orderdetail.dto.OrderDetailResponseDto;
import com.example.ordering_lecture.orderdetail.dto.OrderDetailUpdateDto;
import com.example.ordering_lecture.orderdetail.entity.OrderDetail;
import com.example.ordering_lecture.orderdetail.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }
    @Transactional
    public void updateReview(Long orderDetailId){
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_ORDERDETAIL)
        );
        System.out.println(orderDetailId);
        orderDetail.updateReview();
    }

    @Transactional
    public OrderDetailResponseDto updateOrder(OrderDetailUpdateDto orderDetailUpdateDto) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailUpdateDto.getId()).orElseThrow();
        orderDetail.updateStatue(orderDetailUpdateDto.getStatue());
        return OrderDetailResponseDto.toDto(orderDetail);
    }
}

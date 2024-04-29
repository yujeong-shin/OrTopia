package com.example.ordering_lecture.orderdetail.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.order.dto.OrderResponseDto;
import com.example.ordering_lecture.order.dto.OrderUpdateDto;
import com.example.ordering_lecture.order.entity.Ordering;
import com.example.ordering_lecture.orderdetail.dto.*;
import com.example.ordering_lecture.orderdetail.entity.OrderDetail;
import com.example.ordering_lecture.orderdetail.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }
    // 일별 구매 금액을 위한 데이터
    public List<BuyerGraphPriceData> getBuyerGraphPriceData(String email) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusWeeks(2);
        System.out.println("startDate = " + startDate);
        System.out.println("endDate = " + endDate);
        return orderDetailRepository.findSumPriceByDateBetweenAndStatueAndEmail(startDate, endDate, email);
    }
    // 일별 구매 건수를 위한 데이터
    public List<BuyerGraphCountData> getBuyerGraphCountData(String email) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusWeeks(2);
        return orderDetailRepository.findCompletedOrdersByEmailAndDateRange(startDate, endDate, email);
    }
    // 일별 판매 금액을 위한 데이터
    public List<SellerGraphPriceData> getSellerGraphPriceData(Long sellerId) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusWeeks(2);
        return orderDetailRepository.findSalesData(startDate, endDate, sellerId);
    }
    // 일별 판매 건수를 위한 데이터
    public List<SellerGraphCountData> getSellerGraphCountData(Long sellerId) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusWeeks(2);
        return orderDetailRepository.findSalesDataBySellerIdAndDateRange(startDate, endDate, sellerId);
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

package com.example.ordering_lecture.order;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.order.dto.*;
import com.example.ordering_lecture.order.service.OrderingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderingController {
    private final OrderingService orderingService;

    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }

    //주문 생성
    @PostMapping("/create")
    public Object createOrder(@RequestBody OrderRequestDto  orderRequestDto) {
        return orderingService.createOrder(orderRequestDto);
    }

    // 주문 수정
    @PatchMapping("/update")
    public Object updateOrder(@RequestBody OrderUpdateDto orderUpdateDto){
        return orderingService.updateOrder(orderUpdateDto);
    }
    // 전체 주문 조회
    @GetMapping("/all_order")
    public Object showAllOrder(){
        return orderingService.showAllOrder();
    }
    // 전체 주문 + 디테일 조회
    @GetMapping("/all_detail_order")
    public Object showAllOrdersDetail(){
        return orderingService.showAllOrdersDetail();
    }
    // 특정 회원의 전체 주문 조회
    @GetMapping("/my_order_detail")
    public ResponseEntity<OrTopiaResponse> showMyOrders(@RequestHeader("myEmail") String email){
        List<OrderResponseDto> orderResponseDtos = orderingService.showMyOrders(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", orderResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 특정 회원 전체 주문 조회 + 디테일 조회
    @GetMapping("/all_my_order_detail")
    public ResponseEntity<OrTopiaResponse> showOrders(@RequestHeader("myEmail") String email){
        List<OrderResponseDto> orderResponseDtos = orderingService.showMyOrdersDetail(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", orderResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    @GetMapping("/total_price")
    public ResponseEntity<OrTopiaResponse> totalPrice(@RequestHeader("myEmail") String email){
        List<BuyerGraphPriceData> buyerGraphPriceDatas = orderingService.getBuyerGraphPriceData(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",buyerGraphPriceDatas);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
    @GetMapping("/total_count")
    public ResponseEntity<OrTopiaResponse> totalCount(@RequestHeader("myEmail") String email){
        List<BuyerGraphCountData> buyerGraphCountDatas = orderingService.getBuyerGraphCountData(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",buyerGraphCountDatas);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
}

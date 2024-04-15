package com.example.ordering_lecture.order;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.order.dto.OrderRequestDto;
import com.example.ordering_lecture.order.dto.OrderResponseDto;
import com.example.ordering_lecture.order.dto.OrderUpdateDto;
import com.example.ordering_lecture.order.service.OrderingService;
import com.example.ordering_lecture.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderingController {
    private final OrderingService orderingService;
    private final RedisService redisService;
    public OrderingController(OrderingService orderingService, RedisService redisService) {
        this.orderingService = orderingService;
        this.redisService = redisService;
    }

    //주문 생성
    @PostMapping("/create")
    public ResponseEntity<OrTopiaResponse> createOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestHeader("myEmail")String email) {
        if(redisService.getValues(email).equals(orderRequestDto.getPgToken())){
            OrderResponseDto orderResponseDto = orderingService.createOrder(orderRequestDto);
            OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success",orderResponseDto);
            return new ResponseEntity<>(orTopiaResponse, HttpStatus.CREATED);
        }
            throw new OrTopiaException(ErrorCode.ACCESS_DENIED);
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
    @GetMapping("/my_order_detail/{email}")
    public Object showMyOrders(@PathVariable String email){
        return orderingService.showMyOrders(email);
    }
    // 특정 회원 전체 주문 조회 + 디테일 조회
    @GetMapping("/all_my_order_detail/{email}")
    public Object showOrders(@PathVariable String email){
        return orderingService.showMyOrdersDetail(email);
    }

}

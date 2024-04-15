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

import java.util.List;

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
    public ResponseEntity<OrTopiaResponse> updateOrder(@RequestBody OrderUpdateDto orderUpdateDto){
        OrderResponseDto orderResponseDto = orderingService.updateOrder(orderUpdateDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("update success",orderResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 전체 주문 조회
    @GetMapping("/all_order")
    public ResponseEntity<OrTopiaResponse> showAllOrder(){
        List<OrderResponseDto> orderResponseDtos =  orderingService.showAllOrder();
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",orderResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 전체 주문 + 디테일 조회
    @GetMapping("/all_detail_order")
    public ResponseEntity<OrTopiaResponse> showAllOrdersDetail(){
        List<OrderResponseDto> orderResponseDtos = orderingService.showAllOrdersDetail();
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",orderResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 특정 회원의 전체 주문 조회
    @GetMapping("/my_order_detail/{email}")
    public ResponseEntity<OrTopiaResponse> showMyOrders(@PathVariable String email){
        List<OrderResponseDto> orderResponseDtos = orderingService.showMyOrders(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",orderResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 특정 회원 전체 주문 조회 + 디테일 조회
    @GetMapping("/all_my_order_detail/{email}")
    public ResponseEntity<OrTopiaResponse> showOrders(@PathVariable String email){
        List<OrderResponseDto> orderResponseDtos = orderingService.showMyOrdersDetail(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",orderResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }

}

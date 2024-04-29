package com.example.ordering_lecture.order;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.order.dto.*;
import com.example.ordering_lecture.order.service.OrderingService;
import com.example.ordering_lecture.orderdetail.dto.BuyerGraphCountData;
import com.example.ordering_lecture.orderdetail.dto.BuyerGraphPriceData;
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
    public ResponseEntity<OrTopiaResponse> createOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestHeader("myEmail")String email) {
        OrderResponseDto orderResponseDto = orderingService.createOrder(orderRequestDto,email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success",orderResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.CREATED);

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
    // 판매자가 자신의 판매 내역 조회
    @GetMapping("/mySales")
    public ResponseEntity<OrTopiaResponse> findMyAllItem(@RequestHeader("myEmail") String email, @RequestHeader("myRole") String role){
        if(!role.equals("SELLER")){
            throw new OrTopiaException(ErrorCode.UN_AUTHORIZE_ERROR);
        }
        List<OrderResponseForSellerDto> orderResponseForSellerDtos = orderingService.findMyAllSales(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",orderResponseForSellerDtos);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
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
//    @GetMapping("/total_price/seller")
//    public ResponseEntity<OrTopiaResponse> totalPriceBySeller(@RequestHeader("myEmail") String email){
//        Long sellerId = feignClient.findIdByMemberEmail(email);
//        List<SellerGraphPriceData> sellerGraphPriceDatas = orderingService.getSellerGraphPriceData(sellerId);
//        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",sellerGraphPriceDatas);
//        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
//    }
//    @GetMapping("/total_count/seller")
//    public ResponseEntity<OrTopiaResponse> totalCountBySeller(@RequestHeader("myEmail") String email){
//        Long sellerId = feignClient.findIdByMemberEmail(email);
//        List<SellerGraphCountData> sellerGraphCountDatas = orderingService.getSellerGraphCountData(sellerId);
//        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",sellerGraphCountDatas);
//        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
//    }
}
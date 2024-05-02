package com.example.ordering_lecture.orderdetail.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.feign.FeignClient;
import com.example.ordering_lecture.orderdetail.dto.*;
import com.example.ordering_lecture.orderdetail.service.OrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final FeignClient feignClient;

    public OrderDetailController(OrderDetailService orderDetailService, FeignClient feignClient) {
        this.orderDetailService = orderDetailService;
        this.feignClient = feignClient;
    }

    @GetMapping("check/review/{orderDetailId}")
    public ResponseEntity<OrTopiaResponse> checkReview(@PathVariable Long orderDetailId){
        orderDetailService.updateReview(orderDetailId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("update success");
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    @PostMapping("updateStatus")
    public ResponseEntity<OrTopiaResponse> updateStatue(@RequestBody OrderDetailUpdateDto orderDetailUpdateDto) {
        orderDetailService.updateOrder(orderDetailUpdateDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("update success");
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }

    @GetMapping("/total_price")
    public ResponseEntity<OrTopiaResponse> totalPrice(@RequestHeader("myEmail") String email){
        List<BuyerGraphPriceData> buyerGraphPriceDatas = orderDetailService.getBuyerGraphPriceData(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",buyerGraphPriceDatas);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
    @GetMapping("/total_count")
    public ResponseEntity<OrTopiaResponse> totalCount(@RequestHeader("myEmail") String email){
        List<BuyerGraphCountData> buyerGraphCountDatas = orderDetailService.getBuyerGraphCountData(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",buyerGraphCountDatas);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
    @GetMapping("/total_price/seller")
    public ResponseEntity<OrTopiaResponse> totalPriceBySeller(@RequestHeader("myEmail") String email){
        Long sellerId = feignClient.searchIdByEmail(email);
        List<SellerGraphPriceData> sellerGraphPriceDatas = orderDetailService.getSellerGraphPriceData(sellerId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",sellerGraphPriceDatas);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
    @GetMapping("/total_count/seller")
    public ResponseEntity<OrTopiaResponse> totalCountBySeller(@RequestHeader("myEmail") String email){
        Long sellerId = feignClient.searchIdByEmail(email);
        List<SellerGraphCountData> sellerGraphCountDatas = orderDetailService.getSellerGraphCountData(sellerId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",sellerGraphCountDatas);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
    @GetMapping("/each_item_price/seller")
    public ResponseEntity<OrTopiaResponse> eachItemPriceBySeller(@RequestHeader("myEmail") String email){
        Long sellerId = feignClient.searchIdByEmail(email);
        List<SellerGraphItemPriceData> sellerGraphItemPriceData = orderDetailService.getSellerEachItemPriceData(sellerId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",sellerGraphItemPriceData);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
    @GetMapping("/buyer_gender/seller")
    public ResponseEntity<OrTopiaResponse> buyerGenderBySeller(@RequestHeader("myEmail") String email){
        Long sellerId = feignClient.searchIdByEmail(email);
        Map<String, Long> sellerGraphItemGenderData = orderDetailService.getGenderData(sellerId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", sellerGraphItemGenderData);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    @GetMapping("/buyer_age/seller")
    public ResponseEntity<OrTopiaResponse> buyerAgeBySeller(@RequestHeader("myEmail") String email){
        Long sellerId = feignClient.searchIdByEmail(email);
        Map<Byte, Long> sellerGraphItemAgeData = orderDetailService.getAgeData(sellerId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", sellerGraphItemAgeData);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 주문 상태 수정
    @PatchMapping("/update")
    public ResponseEntity<OrTopiaResponse> updateOrder(@RequestBody OrderDetailUpdateDto orderDetailUpdateDto){
        OrderDetailResponseDto orderDetailResponseDto = orderDetailService.updateOrder(orderDetailUpdateDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("update success",orderDetailResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
}

package com.example.ordering_lecture.orderdetail;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.orderdetail.dto.OrderDetailUpdateDto;
import com.example.ordering_lecture.orderdetail.service.OrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
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
}

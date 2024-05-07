package com.example.ordering_lecture.coupon.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.coupon.dto.CouponRequestDto;
import com.example.ordering_lecture.coupon.dto.CouponResponseDto;
import com.example.ordering_lecture.coupon.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }
    @PostMapping("/coupon/create")
    public ResponseEntity<OrTopiaResponse> createCoupon(@RequestBody CouponRequestDto couponRequestDto,@RequestHeader("myEmail") String email) {
        List<CouponResponseDto> couponResponseDtos = couponService.createCoupon(couponRequestDto);
        couponService.createMessage(email,couponResponseDtos);
        return new ResponseEntity<>(new OrTopiaResponse("create success", couponResponseDtos), HttpStatus.CREATED);
    }

    @GetMapping("/coupon/{itemId}")
    public ResponseEntity<OrTopiaResponse> getCoupon(@PathVariable Long itemId) {
        List<CouponResponseDto> couponResponseDtos = couponService.getCoupon(itemId);
        return new ResponseEntity<>(new OrTopiaResponse("read success", couponResponseDtos), HttpStatus.OK);
    }
}
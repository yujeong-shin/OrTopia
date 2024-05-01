package com.example.ordering_lecture.coupon.controller;

import com.example.ordering_lecture.coupon.dto.CouponRequestDto;
import com.example.ordering_lecture.coupon.dto.CouponResponseDto;
import com.example.ordering_lecture.coupon.service.CouponService;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }
    @PostMapping("/coupon/create")
    public Object createCoupon(@RequestBody CouponRequestDto couponRequestDto){
        return couponService.createCoupon(couponRequestDto);
    }
    @GetMapping("/coupon/{itemId}")
    public List<CouponResponseDto> getCoupon(@PathVariable Long itemId) {
        return couponService.getCoupon(itemId);
    }
}
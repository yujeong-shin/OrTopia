package com.example.ordering_lecture.coupondetail.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailRequestDto;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailResponseDto;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailUpdateDto;
import com.example.ordering_lecture.coupondetail.service.CouponDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CouponDetailController {
    private final CouponDetailService couponDetailService;

    public CouponDetailController(CouponDetailService couponDetailService) {
        this.couponDetailService = couponDetailService;
    }

    @PostMapping("/coupondetail/create")
    public ResponseEntity<OrTopiaResponse> createCouponDetail(@RequestBody CouponDetailRequestDto couponDetailRequestDto, @RequestHeader("myEmail") String email) {
        CouponDetailResponseDto couponDetailResponseDto = couponDetailService.createCouponDetail(couponDetailRequestDto, email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success", couponDetailResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.CREATED);
    }
    @GetMapping("/coupondetail")
    public ResponseEntity<OrTopiaResponse> getAllCouponsBySellerId(@RequestHeader("myEmail") String email) {
        List<CouponDetailResponseDto> couponDetails = couponDetailService.findAllBySellerId(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", couponDetails);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    @PatchMapping("/couponupdate/{id}")
    public ResponseEntity<OrTopiaResponse> updateCoupon(@PathVariable Long id, @RequestBody CouponDetailUpdateDto couponDetailUpdateDto) {
        CouponDetailResponseDto couponDetailResponseDto = couponDetailService.updateCoupons(id, couponDetailUpdateDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("update success", couponDetailResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    @DeleteMapping("/coupon/delete/{id}")
    public ResponseEntity<OrTopiaResponse> deleteCoupon(@PathVariable Long id) {
        couponDetailService.deleteCoupon(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("delete success", null);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
}

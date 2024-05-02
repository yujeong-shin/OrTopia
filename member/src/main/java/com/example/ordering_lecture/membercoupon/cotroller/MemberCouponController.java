package com.example.ordering_lecture.membercoupon.cotroller;

import com.example.ordering_lecture.address.dto.AddressRequestDto;
import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailRequestDto;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailResponseDto;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponRequestDto;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponResponseDto;
import com.example.ordering_lecture.membercoupon.service.MemberCouponService;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberCouponController {
    private final MemberCouponService memberCouponService;
    public MemberCouponController(MemberCouponService memberCouponService) {
        this.memberCouponService = memberCouponService;
    }
    @PostMapping("/coupons")
    public ResponseEntity<OrTopiaResponse> addCoupon(@RequestHeader("myEmail") String email, @RequestBody MemberCouponRequestDto request) {
        MemberCouponResponseDto response = memberCouponService.addCoupon(email, request);
        return new ResponseEntity<>(new OrTopiaResponse("create success", response), HttpStatus.CREATED);
    }
    @GetMapping("/member/coupons")
    public ResponseEntity<OrTopiaResponse> getAllCoupons(@RequestHeader("myEmail") String email) {
        List<MemberCouponResponseDto> coupons = memberCouponService.findAllCouponsByEmail(email);
        return new ResponseEntity<>(new OrTopiaResponse("read success", coupons), HttpStatus.OK);
    }
    @DeleteMapping("/member/coupons/{couponId}")
    public ResponseEntity<OrTopiaResponse> useCoupon(@RequestHeader("myEmail") String email, @PathVariable Long couponId) {
        memberCouponService.useCoupon(email, couponId);
        return new ResponseEntity<>(new OrTopiaResponse("delete success", null), HttpStatus.OK);
    }
}
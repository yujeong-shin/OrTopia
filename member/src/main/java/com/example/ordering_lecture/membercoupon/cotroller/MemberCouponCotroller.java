package com.example.ordering_lecture.membercoupon.cotroller;

import com.example.ordering_lecture.address.dto.AddressRequestDto;
import com.example.ordering_lecture.address.dto.AddressResponseDto;
import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.coupon.dto.CouponRequestDto;
import com.example.ordering_lecture.coupondetail.domain.CouponDetail;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailResponseDto;
import com.example.ordering_lecture.member.dto.Buyer.MemberResponseDto;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponRequestDto;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponResponseDto;
import com.example.ordering_lecture.membercoupon.service.MemberCouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberCouponCotroller {
    public final MemberCouponService memberCouponService;

    public MemberCouponCotroller(MemberCouponService memberCouponService) {
        this.memberCouponService = memberCouponService;
    }

    @PostMapping("/assign")
    public ResponseEntity<OrTopiaResponse> assignCoupon(@RequestBody MemberCouponRequestDto memberCouponRequestDto) {
        MemberCouponResponseDto memberCouponResponseDto = memberCouponService.assignCoupon(memberCouponRequestDto);
        OrTopiaResponse response = new OrTopiaResponse("쿠폰 할당 성공", memberCouponResponseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/mycoupons")
    public ResponseEntity<OrTopiaResponse> getMyCoupons(@RequestHeader("myEmail") String email) {
        List<MemberCouponResponseDto> coupons = memberCouponService.findMyCoupons(email);
        OrTopiaResponse response = new OrTopiaResponse("쿠폰 조회 완료", coupons);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/use/{id}")
    public ResponseEntity<OrTopiaResponse> useCoupon(@PathVariable Long id) {
        memberCouponService.useCoupon(id);
        return new ResponseEntity<>(new OrTopiaResponse("쿠폰 사용 성공"), HttpStatus.OK);
    }
}
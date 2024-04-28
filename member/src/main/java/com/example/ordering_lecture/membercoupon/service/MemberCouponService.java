package com.example.ordering_lecture.membercoupon.service;

import com.example.ordering_lecture.coupon.domain.Coupon;
import com.example.ordering_lecture.coupon.repository.CouponRepository;
import com.example.ordering_lecture.member.repository.MemberRepository;
import com.example.ordering_lecture.membercoupon.domain.MemberCoupon;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponRequestDto;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponResponseDto;
import com.example.ordering_lecture.membercoupon.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberCouponService {
    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public MemberCouponService(MemberCouponRepository memberCouponRepository, CouponRepository couponRepository, MemberRepository memberRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
    }

    public MemberCouponResponseDto addCoupon(String email, MemberCouponRequestDto request) {
        Long memberId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"))
                .getId();
        Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .memberId(memberId)
                .coupon(coupon)
                .build();

        memberCouponRepository.save(memberCoupon);
        return MemberCouponResponseDto.toDto(memberCoupon);
    }
    public List<MemberCouponResponseDto> findAllCouponsByEmail(String email) {
        Long memberId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"))
                .getId();
        List<MemberCoupon> coupons = memberCouponRepository.findByMemberId(memberId);
        return coupons.stream()
                .map(MemberCouponResponseDto::toDto)
                .collect(Collectors.toList());
    }
    public void useCoupon(String email, Long couponId) {
        Long memberId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"))
                .getId();
        MemberCoupon coupon = memberCouponRepository.findByMemberIdAndCouponId(memberId, couponId)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
        memberCouponRepository.delete(coupon);
    }
}
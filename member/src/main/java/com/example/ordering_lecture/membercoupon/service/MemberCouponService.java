package com.example.ordering_lecture.membercoupon.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.coupondetail.domain.CouponDetail;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailResponseDto;
import com.example.ordering_lecture.coupondetail.repository.CouponDetailRepository;
import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.member.repository.MemberRepository;
import com.example.ordering_lecture.membercoupon.domain.MemberCoupon;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponRequestDto;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponResponseDto;
import com.example.ordering_lecture.membercoupon.repository.MemberCouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberCouponService {
    private final MemberCouponRepository memberCouponRepository;
    private final  MemberRepository memberRepository;
    private final CouponDetailRepository couponDetailRepository;

    public MemberCouponService(MemberCouponRepository memberCouponRepository, MemberRepository memberRepository, CouponDetailRepository couponDetailRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.memberRepository = memberRepository;
        this.couponDetailRepository = couponDetailRepository;
    }
    @Transactional
    public MemberCouponResponseDto assignCoupon(MemberCouponRequestDto memberCouponRequestDto) {
        Member member = memberRepository.findById(memberCouponRequestDto.getMemberId())
                .orElseThrow(() -> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER));
        CouponDetail couponDetail = couponDetailRepository.findById(memberCouponRequestDto.getCouponDetailId())
                .orElseThrow(() -> new OrTopiaException(ErrorCode.COUPON_NOT_FOUND));
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .member(member)
                .couponDetail(couponDetail)
                .build();
        memberCoupon = memberCouponRepository.save(memberCoupon);
        return new MemberCouponResponseDto(memberCoupon.getId(), CouponDetailResponseDto.toDto(memberCoupon.getCouponDetail()));
    }
    public List<MemberCouponResponseDto> findMyCoupons(String email) {
        log.info("Fetching coupons for email: {}", email);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Member not found for email: {}", email);
                    return new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER);
                });
        List<MemberCoupon> coupons = memberCouponRepository.findAllByMember(member);
        if (coupons.isEmpty()) {
            return Collections.emptyList();
        }
        return coupons.stream()
                .map(mc -> new MemberCouponResponseDto(
                        mc.getId(),
                        CouponDetailResponseDto.toDto(mc.getCouponDetail())
                )).collect(Collectors.toList());
    }
    @Transactional
    public void useCoupon(Long id) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(id)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.COUPON_NOT_FOUND));
        memberCouponRepository.delete(memberCoupon);
    }
}
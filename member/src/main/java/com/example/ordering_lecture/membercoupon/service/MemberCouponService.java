package com.example.ordering_lecture.membercoupon.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.coupon.domain.Coupon;
import com.example.ordering_lecture.coupon.repository.CouponRepository;
import com.example.ordering_lecture.member.repository.MemberRepository;
import com.example.ordering_lecture.membercoupon.domain.MemberCoupon;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponRequestDto;
import com.example.ordering_lecture.membercoupon.dto.MemberCouponResponseDto;
import com.example.ordering_lecture.membercoupon.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    public MemberCouponResponseDto addCoupon(String email, MemberCouponRequestDto request) throws OrTopiaException {
        Long memberId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER))
                .getId();
        List<MemberCoupon> memberCoupons = memberCouponRepository.findAllByMemberId(memberId);
        for(MemberCoupon memberCoupon : memberCoupons){
            if(memberCoupon.getCoupon().getId().equals(request.getCouponId())){
                throw new OrTopiaException(ErrorCode.COUPON_ALREADY_HAVE);
            }
        }
        Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElseThrow(() -> new OrTopiaException(ErrorCode.COUPON_NOT_FOUND));
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .memberId(memberId)
                .coupon(coupon)
                .build();
        int nowCouponStock = coupon.getCouponDetail().getFirstCome();
        coupon.getCouponDetail().updateFirstCome(nowCouponStock-1);
        memberCouponRepository.save(memberCoupon);
        return MemberCouponResponseDto.toDto(memberCoupon);
    }

    public List<MemberCouponResponseDto> findAllCouponsByEmail(String email) throws OrTopiaException {
        Long memberId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER))
                .getId();
        List<MemberCoupon> coupons = memberCouponRepository.findByMemberId(memberId);
        return coupons.stream()
                .map(MemberCouponResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public void useCoupon(String email, Long couponId) throws OrTopiaException {
        Long memberId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER))
                .getId();
        MemberCoupon coupon = memberCouponRepository.findByMemberIdAndCouponId(memberId, couponId)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.COUPON_NOT_FOUND));

        memberCouponRepository.delete(coupon);
    }
}
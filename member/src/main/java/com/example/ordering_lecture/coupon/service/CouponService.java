package com.example.ordering_lecture.coupon.service;

import com.example.ordering_lecture.coupon.domain.Coupon;
import com.example.ordering_lecture.coupon.dto.CouponRequestDto;
import com.example.ordering_lecture.coupon.dto.CouponResponseDto;
import com.example.ordering_lecture.coupon.repository.CouponRepository;
import com.example.ordering_lecture.coupondetail.domain.CouponDetail;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailRequestDto;
import com.example.ordering_lecture.coupondetail.repository.CouponDetailRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponDetailRepository couponDetailRepository;

    public CouponService(CouponRepository couponRepository, CouponDetailRepository couponDetailRepository) {
        this.couponRepository = couponRepository;
        this.couponDetailRepository = couponDetailRepository;
    }

    public List<CouponResponseDto> createCoupon(CouponRequestDto couponRequestDto) {
        CouponDetail couponDetail = couponDetailRepository.findById(couponRequestDto.getCouponDetailId())
                .orElseThrow(() -> new RuntimeException("쿠폰 세부 정보를 찾을 수 없습니다."));

        List<Coupon> coupons = couponRequestDto.getItemId().stream()
                .map(itemId -> Coupon.builder()
                        .itemId(itemId)
                        .couponDetail(couponDetail)
                        .build())
                .collect(Collectors.toList());

        couponRepository.saveAll(coupons); // 일괄 저장

        return coupons.stream()
                .map(coupon -> CouponResponseDto.toDto(coupon, couponDetail))
                .collect(Collectors.toList());
    }
}
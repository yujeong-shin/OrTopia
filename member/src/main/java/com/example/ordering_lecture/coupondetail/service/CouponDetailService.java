package com.example.ordering_lecture.coupondetail.service;

import com.example.ordering_lecture.address.domain.Address;
import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.coupondetail.domain.CouponDetail;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailRequestDto;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailResponseDto;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailUpdateDto;
import com.example.ordering_lecture.coupondetail.repository.CouponDetailRepository;
import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponDetailService {
    private final CouponDetailRepository couponDetailRepository;
    private final MemberRepository memberRepository;

    public CouponDetailService(CouponDetailRepository couponDetailRepository, MemberRepository memberRepository) {
        this.couponDetailRepository = couponDetailRepository;
        this.memberRepository = memberRepository;
    }
    public CouponDetailResponseDto createCouponDetail(CouponDetailRequestDto couponDetailRequestDto, String email) throws OrTopiaException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER));
        CouponDetail couponDetail = couponDetailRequestDto.toEntity(member);
        try {
            CouponDetail savedCouponDetail = couponDetailRepository.save(couponDetail);
            return CouponDetailResponseDto.toDto(savedCouponDetail);
        } catch (Exception e) {
            throw new OrTopiaException(ErrorCode.COUPON_CREATION_FAILED);
        }
    }
    public List<CouponDetailResponseDto> findAllBySellerId(String email) throws OrTopiaException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER));
        List<CouponDetail> couponDetails = couponDetailRepository.findAllByMember(member);
        return couponDetails.stream()
                .map(CouponDetailResponseDto::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public CouponDetailResponseDto updateCoupons(Long id, CouponDetailUpdateDto couponDetailUpdateDto) throws OrTopiaException {
        CouponDetail couponDetail = couponDetailRepository.findById(id)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.COUPON_NOT_FOUND));
        couponDetail = couponDetailUpdateDto.toUpdate(couponDetail);
        return CouponDetailResponseDto.toDto(couponDetail);
    }

    public void deleteCoupon(Long id) throws OrTopiaException {
        CouponDetail couponDetail = couponDetailRepository.findById(id)
                .orElseThrow(() -> new OrTopiaException(ErrorCode.COUPON_NOT_FOUND));
        couponDetailRepository.deleteById(id);
    }
}

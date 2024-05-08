package com.example.ordering_lecture.membercoupon.repository;

import com.example.ordering_lecture.membercoupon.domain.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    List<MemberCoupon> findByMemberId(Long memberId);
    Optional<MemberCoupon> findByMemberIdAndCouponId(Long memberId, Long couponId);

    List<MemberCoupon> findAllByMemberId(Long memberId);
}
package com.example.ordering_lecture.coupondetail.repository;

import com.example.ordering_lecture.coupondetail.domain.CouponDetail;
import com.example.ordering_lecture.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponDetailRepository extends JpaRepository<CouponDetail,Long> {
    List<CouponDetail> findAllByMember(Member member);
}

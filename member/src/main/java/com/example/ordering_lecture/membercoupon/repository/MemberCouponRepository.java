package com.example.ordering_lecture.membercoupon.repository;

import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.membercoupon.domain.MemberCoupon;
import io.lettuce.core.ScanIterator;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    List<MemberCoupon> findAllByMember(Member member);
}

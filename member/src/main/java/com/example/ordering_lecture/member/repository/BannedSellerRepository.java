package com.example.ordering_lecture.member.repository;

import com.example.ordering_lecture.member.domain.BannedSeller;
import com.example.ordering_lecture.member.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannedSellerRepository extends JpaRepository<BannedSeller, Long> {
    List<BannedSeller> findByDelYNFalse();
}

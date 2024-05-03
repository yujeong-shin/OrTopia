package com.example.ordering_lecture.item.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ortopia-member-service", url ="http://ortopia-member-service.default.svc.cluster.local")
public interface MemberServiceClient {
    @GetMapping("/member/search/{email}")
    Long searchIdByEmail(@PathVariable("email") String email);

    @GetMapping("/member/search/name/{email}")
    String searchNameByEmail(@PathVariable("email") String email);

    // 판매자의 sellerId로 멤버 emails 조회.
    @GetMapping("/seller/search/emails/{sellerId}")
    List<String> searchEmailsBySellerId(@PathVariable("sellerId") Long sellerId);

    // 판매작의 sellerId로 회사명 조회.
    @GetMapping("/seller/noLogin/fein/{id}")
    String findSellerName(@PathVariable Long id);

    // 구매자의 id를 이메일로 조회
    @GetMapping("/member/search/id/{email}")
    Long findMemberIdByEmail(@PathVariable("email") String buyerEmail);
}

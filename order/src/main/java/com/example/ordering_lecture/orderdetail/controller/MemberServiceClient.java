package com.example.ordering_lecture.orderdetail.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ortopia-member-service", url ="http://ortopia-member-service.default.svc.cluster.local")
public interface MemberServiceClient {
    @GetMapping(value="/member/search/{email}")
    Long searchIdByEmail(@PathVariable("email") String email);
    @PostMapping(value="/member/gender/{email}")
    String searchGenderByEmail(@PathVariable(name="email") String email);
    @PostMapping(value="/member/age/{email}")
    byte searchAgeByEmail(@PathVariable(name="email") String email);
}
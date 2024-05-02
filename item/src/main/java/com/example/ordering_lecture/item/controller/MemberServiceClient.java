package com.example.ordering_lecture.item.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ortopia-member-service", url ="http://ortopia-member-service.default.svc.cluster.local")
public interface MemberServiceClient {
    @GetMapping("/member/search/{email}")
    Long searchIdByEmail(@PathVariable("email") String email);

    @GetMapping("/member/search/name/{email}")
    String searchNameByEmail(@PathVariable("email") String email);
}

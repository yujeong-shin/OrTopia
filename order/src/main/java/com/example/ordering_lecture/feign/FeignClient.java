package com.example.ordering_lecture.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.cloud.openfeign.FeignClient(name = "ortopia-member-service", url ="http://ortopia-member-service.default.svc.cluster.local")
public interface FeignClient {
    @GetMapping(value="/member/{email}/memberId")
    Long findIdByMemberEmail(@PathVariable("email") String email);
    @GetMapping(value="/member/search/{email}")
    Long searchIdByEmail(@PathVariable("email") String email);
    @PostMapping(value="/member/gender/{email}")
    String searchGenderByEmail(@PathVariable(name="email") String email);
    @PostMapping(value="/member/age/{email}")
    byte searchAgeByEmail(@PathVariable(name="email") String email);
}

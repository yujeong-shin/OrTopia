package com.example.ordering_lecture.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    @GetMapping(value="/member/search/{email}")
    Long searchIdByEmail(@PathVariable("email") String email);
}
